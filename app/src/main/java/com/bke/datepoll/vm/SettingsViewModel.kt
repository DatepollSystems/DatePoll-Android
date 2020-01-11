package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel: BaseViewModel() {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = userRepo.user
    val updateUserState = MutableLiveData<ENetworkState>()
    val loadUserState = MutableLiveData<ENetworkState>()

    fun loadUserdata(){
        scope.launch {
            userRepo.getUser(loadUserState, true)
        }
    }

    fun updateUser(u: UpdateUserRequest) {
        scope.launch {
            userRepo.updateUser(updateUserState, u)
        }
    }
}
