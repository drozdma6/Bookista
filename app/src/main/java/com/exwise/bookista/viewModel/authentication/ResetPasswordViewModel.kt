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
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class ResetPasswordViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val email = MutableLiveData<String>()

    val recoverAccountEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(email) {
            postValue(it.isNotBlank())
        }
    }

    private val _recoverAccountPressed = MutableLiveData<ButtonPressedEvent>()
    val recoverAccountPressed: LiveData<ButtonPressedEvent> = _recoverAccountPressed

    private val _recoverAccount = MutableLiveData<RecoverAccountEvent>()
    val recoverAccount: LiveData<RecoverAccountEvent> = _recoverAccount

    private val _goBackToLogin = MutableLiveData<ButtonPressedEvent>()
    val goBackToLogin: LiveData<ButtonPressedEvent> = _goBackToLogin

    fun recoverAccountPressed() {
        _recoverAccountPressed.postValue(ButtonPressedEvent.Pressed())
        recoverAccount()
    }

    private fun recoverAccount() {
        email.value?.let { email ->
            if (!EmailValidator.validateEmail(email)) {
                _recoverAccount.postValue(RecoverAccountEvent.Error(RecoverAccountEventError.InvalidEmail()))
                return
            }
            authRepository.sendPasswordResetEmail(email,
                {
                    _recoverAccount.postValue(RecoverAccountEvent.Success())
                },
                { error ->
                    val errorEvent = handleRecoverError(error)
                    _recoverAccount.postValue(RecoverAccountEvent.Error(errorEvent))
                })
        }
    }

    fun alreadyHaveAnAccountPressed() {
        _goBackToLogin.postValue(ButtonPressedEvent.Pressed())
    }

    private fun handleRecoverError(throwable: Throwable?): RecoverAccountEventError {
        return when (throwable) {
            is FirebaseAuthInvalidUserException -> RecoverAccountEventError.InvalidUser()
            is FirebaseNetworkException -> RecoverAccountEventError.NetworkError()
            is FirebaseTooManyRequestsException -> RecoverAccountEventError.TooManyRequests()
            else -> {
                RecoverAccountEventError.UnknownError()
            }
        }
    }
}

sealed class RecoverAccountEvent : Event() {
    class Success : RecoverAccountEvent()
    class Error(val error: RecoverAccountEventError) : RecoverAccountEvent()
}

sealed class RecoverAccountEventError : Event() {
    class InvalidEmail : RecoverAccountEventError()
    class InvalidUser : RecoverAccountEventError()
    class NetworkError : RecoverAccountEventError()
    class UnknownError : RecoverAccountEventError()
    class TooManyRequests : RecoverAccountEventError()
}