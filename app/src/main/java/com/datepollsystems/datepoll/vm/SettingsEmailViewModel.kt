package com.datepollsystems.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject

class SettingsEmailViewModel : BaseViewModel(){

    private val userRepo: UserRepository by inject()

    val email = MutableLiveData<String>()

    val emails = userRepo.emails

    val saveEmailsState =  MutableLiveData<ENetworkState>()

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