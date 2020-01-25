package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.database.model.EmailAddressDbModel
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsEmailViewModel : BaseViewModel(){

    private val userRepo: UserRepository by inject()

    val email = MutableLiveData<String>()

    val emails = userRepo.emails

    val saveEmailsState =  MutableLiveData<ENetworkState>()

    fun addEmail(e: String) {
        scope.launch {
            userRepo.addEmail(e)
        }
    }

    fun deleteEmail(e: String) {
        scope.launch {
            userRepo.removeEmail(e)
        }
    }

    fun saveEmails() {
        scope.launch {
            userRepo.saveEmailsToServer(saveEmailsState)
        }
    }
}