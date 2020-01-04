package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel: BaseViewModel() {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = userRepo.user
    val userLoaded = MutableLiveData<Boolean>()
    val userUpdated = MutableLiveData<Boolean>()

    fun loadUserdata(){
        scope.launch {
            userRepo.getUser(true)
            userLoaded.postValue(true)
        }
    }

    fun updateUser(u: UpdateUserRequest) {
        scope.launch {
            userRepo.updateUser(u)
            userUpdated.postValue(true)
        }
    }
}
