package com.exwise.bookista.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exwise.bookista.domain.repository.AuthRepository

class SettingsFragmentViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _logOut: MutableLiveData<SimpleEvent> =
        MutableLiveData<SimpleEvent>()
    val logOut: LiveData<SimpleEvent> = _logOut

    fun logOut() {
        authRepository.logOut()
        if(authRepository.isUserLoggedIn()) {
            _logOut.postValue(SimpleEvent.Error())
        } else {
            _logOut.postValue(SimpleEvent.Success())
        }
    }

}