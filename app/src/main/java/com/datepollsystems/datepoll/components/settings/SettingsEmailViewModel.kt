package com.datepollsystems.datepoll.components.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsEmailViewModel : ViewModel(), KoinComponent {

    private val userRepo: UserRepository by inject()

    val email = MutableLiveData<String>()

    val emails = userRepo.emails

    val saveEmailsState =  MutableLiveData<ENetworkState?>()

    fun addEmail(e: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.addEmail(e)
        }
    }

    fun deleteEmail(e: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.removeEmail(e)
        }
    }

    fun saveEmails() {
        viewModelScope.launch(Dispatchers.Default) {
            userRepo.saveEmailsToServer(saveEmailsState)
        }
    }
}