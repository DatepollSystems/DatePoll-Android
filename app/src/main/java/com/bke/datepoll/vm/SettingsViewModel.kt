package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.Prefs
import com.bke.datepoll.db.model.UserDbModel
import com.bke.datepoll.repos.SettingsRepository
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SettingsViewModel(
    private val prefs: Prefs,
    private val settingsRepo: SettingsRepository,
    private val userRepo: UserRepository
) : BaseViewModel() {

    val user = MutableLiveData<UserDbModel>()

    fun loadUser(){
        scope.launch {
            val u = userRepo.loadUser()
            user.postValue(u)
        }
    }
}
