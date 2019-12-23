package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.db.model.UserDbModel
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.*


class SettingsViewModel(
    private val prefs: Prefs,
    private val userRepo: UserRepository
) : BaseViewModel() {

    val user = MutableLiveData<UserDbModel>()

}
