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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterFragmentViewModel(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    private val _register = MutableLiveData<RegisterEvent>()
    val register: LiveData<RegisterEvent> = _register

    private val _registerPressed = MutableLiveData<ButtonPressedEvent>()
    val registerPressed: LiveData<ButtonPressedEvent> = _registerPressed

    private val _alreadyHaveAnAccount = MutableLiveData<ButtonPressedEvent>()
    val alreadyHaveAnAccount: LiveData<ButtonPressedEvent> = _alreadyHaveAnAccount

    val registerEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(email) {
            postValue(it.isNotBlank() && password.value?.isNotBlank() == true && confirmPassword.value?.isNotBlank() == true)
        }

        addSource(password) {
            postValue(email.value?.isNotBlank() == true && it.isNotBlank() && confirmPassword.value?.isNotBlank() == true)
        }

        addSource(confirmPassword) {
            postValue(email.value?.isNotBlank() == true && password.value?.isNotBlank() == true && it.isNotBlank())
        }
    }

    fun registerPressed() {
        _registerPressed.postValue(ButtonPressedEvent.Pressed())
        register()
    }

    private fun register() {
        //Validate email and password
        val emailValue = email.value
        val passwordValue = password.value
        val confirmPasswordValue = confirmPassword.value
        if (emailValue == null || passwordValue == null || confirmPasswordValue == null) {
            return
        }
        if (!EmailValidator.validateEmail(emailValue)) {
            RegisterEvent.Error(RegisterEventError.InvalidEmail())
        }
        if (passwordValue.length < 6) {
            RegisterEvent.Error(RegisterEventError.WeakPassword())
        }
        if (!passwordValue.contentEquals(confirmPasswordValue)) {
            RegisterEvent.Error(RegisterEventError.PasswordsDoNotMatch())
        }
        authRepository.register(emailValue, passwordValue,
            {
                //Success Listener
                sendVerificationEmail()
                //Firebase automatically logs in, we want to first send verification email
                auth.signOut()
            },
            { error ->
                //Failure Listener
                val eventError = handleRegisterError(error)
                _register.postValue(RegisterEvent.Error(eventError))
            })
    }

    fun alreadyHaveAnAccountPressed() {
        _alreadyHaveAnAccount.postValue(ButtonPressedEvent.Pressed())
    }

    private fun sendVerificationEmail() {
        auth.currentUser?.let { user ->
            authRepository.sendVerificationEmail(user,
                {
                    _register.postValue(RegisterEvent.Success())
                }, { error ->
                    val errorEvent = handleRegisterError(error)
                    _register.postValue(RegisterEvent.Error(errorEvent))
                })
        }
    }

    private fun handleRegisterError(throwable: Throwable?): RegisterEventError {
        return when (throwable) {
            is FirebaseAuthWeakPasswordException -> RegisterEventError.WeakPassword()
            is FirebaseAuthInvalidCredentialsException -> RegisterEventError.InvalidEmail()
            is FirebaseAuthUserCollisionException -> RegisterEventError.EmailAlreadyUsed()
            is FirebaseTooManyRequestsException -> RegisterEventError.TooManyRequests()
            is FirebaseNetworkException -> RegisterEventError.NetworkError()
            else -> {
                RegisterEventError.UnknownError()
            }
        }
    }
}

sealed class RegisterEvent : Event() {
    class Success : RegisterEvent()
    class Error(val error: RegisterEventError) : RegisterEvent()
}

sealed class RegisterEventError : Event() {
    class InvalidEmail : RegisterEventError()
    class EmailAlreadyUsed : RegisterEventError()
    class WeakPassword : RegisterEventError()
    class PasswordsDoNotMatch : RegisterEventError()
    class TooManyRequests : RegisterEventError()
    class NetworkError : RegisterEventError()
    class UnknownError : RegisterEventError()
}