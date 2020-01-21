package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.database.model.EmailAddressDbModel
import com.bke.datepoll.repos.UserRepository
import org.koin.core.inject

class SettingsEmailViewModel : BaseViewModel(){

    val userRepo: UserRepository by inject()

    val email = MutableLiveData<String>()

    val emails = userRepo.emails
}