package com.exwise.bookista.viewModel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exwise.bookista.domain.livedata.model.Event
import com.exwise.bookista.domain.repository.AuthRepository
import com.exwise.bookista.helper.EmailValidator
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragmentViewModel(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginPressed: MutableLiveData<ButtonPressedEvent> =
        MutableLiveData<ButtonPressedEvent>()
    val loginPressed: LiveData<ButtonPressedEvent> = _loginPressed

    private val _login: MutableLiveData<LoginEvent> =
        MutableLiveData<LoginEvent>()
    val login: LiveData<LoginEvent> = _login

    private val _sendEmail: MutableLiveData<SendEmailEvent> =
        MutableLiveData<SendEmailEvent>()
    val sendEmail: LiveData<SendEmailEvent> = _sendEmail

    private val _signUpPressed: MutableLiveData<ButtonPressedEvent> =
        MutableLiveData<ButtonPressedEvent>()
    val signUpButton: LiveData<ButtonPressedEvent> = _signUpPressed

    private val _forgotPassword: MutableLiveData<ButtonPressedEvent> =
        MutableLiveData<ButtonPressedEvent>()
    val forgotPasswordButton: LiveData<ButtonPressedEvent> = _forgotPassword

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(email) {
            postValue(it.isNotBlank() && password.value?.isNotEmpty() == true)
        }
        addSource(password) {
            postValue(it.isNotBlank() && email.value?.isNotEmpty() == true)
        }
    }

    fun loginPressed() {
        _loginPressed.postValue(ButtonPressedEvent.Pressed())
        login()
    }

    fun sendVerificationEmail() {
        auth.currentUser?.let { user ->
            authRepository.sendVerificationEmail(user,
                {
                    _sendEmail.postValue(SendEmailEvent.Success())
                }, { error ->
                    val errorEvent = handleEmailError(error)
                    _sendEmail.postValue(errorEvent)
                })
        }
    }

    private fun login() {
        val emailValue = email.value
        val passwordValue = password.value
        if (emailValue == null || passwordValue == null) {
            return
        }
        if (!EmailValidator.validateEmail(emailValue)) {
            _login.postValue(LoginEvent.Error(LoginEventError.InvalidEmail()))
        } else {
            authRepository.login(emailValue, passwordValue,
                {
                    //Check if user is verified
                    verifyUserEmail()
                },
                { error ->
                    val errorValue = handleLoginError(error)
                    _login.postValue(LoginEvent.Error(errorValue))
                })
        }
    }

    private fun verifyUserEmail() {
        auth.currentUser?.let { user ->
            if (user.isEmailVerified) {
                _login.postValue(LoginEvent.Success())
            } else {
                _login.postValue(LoginEvent.Error(LoginEventError.UserEmailNotVerified()))
            }
        }
    }

    fun signUp() {
        _signUpPressed.postValue(ButtonPressedEvent.Pressed())
    }

    fun forgotPassword() {
        _forgotPassword.postValue(ButtonPressedEvent.Pressed())
    }

    private fun handleLoginError(throwable: Throwable?): LoginEventError {
        return when (throwable) {
            is FirebaseAuthInvalidUserException -> LoginEventError.BadCredentials()
            is FirebaseAuthInvalidCredentialsException -> LoginEventError.BadCredentials()
            is FirebaseTooManyRequestsException -> LoginEventError.TooManyRequests()
            is FirebaseNetworkException -> LoginEventError.NetworkError()
            else -> {
                LoginEventError.UnknownError()
            }
        }
    }

    private fun handleEmailError(throwable: Throwable?): SendEmailEvent {
        return when (throwable) {
            is FirebaseNetworkException -> SendEmailEvent.Error(SendEmailError.NetworkError())
            is FirebaseTooManyRequestsException -> SendEmailEvent.Error(SendEmailError.TooManyRequests())
            else -> {
                SendEmailEvent.Error(SendEmailError.UnknownError())
            }
        }
    }
}


sealed class SendEmailEvent : Event() {
    class Success : SendEmailEvent()
    class Error(val error: SendEmailError) : SendEmailEvent()
}

sealed class SendEmailError : Event() {
    class UnknownError : SendEmailError()
    class NetworkError : SendEmailError()
    class TooManyRequests : SendEmailError()
}

sealed class LoginEvent : Event() {
    class Success : LoginEvent()
    class Error(val error: LoginEventError) : LoginEvent()
}

sealed class LoginEventError : Event() {
    class InvalidEmail : LoginEventError()
    class BadCredentials : LoginEventError()
    class NetworkError : LoginEventError()
    class UserEmailNotVerified : LoginEventError()
    class UnknownError : LoginEventError()
    class TooManyRequests : LoginEventError()
}

sealed class ButtonPressedEvent : Event() {
    class Pressed : ButtonPressedEvent()
}