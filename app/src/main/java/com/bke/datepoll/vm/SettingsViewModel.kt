package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.model.NewPhoneNumberRequest
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel: BaseViewModel() {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = userRepo.user
    val phoneNumbers = userRepo.phoneNumbers

    val updateUserState = MutableLiveData<ENetworkState>()
    val loadUserState = MutableLiveData<ENetworkState>()
    val addPhoneNumberState = MutableLiveData<ENetworkState>()
    val removePhoneNumberState = MutableLiveData<ENetworkState>()

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

    fun addPhoneNumber(p: NewPhoneNumberRequest){
        scope.launch {
            userRepo.addPhoneNumber(addPhoneNumberState, p)
        }
    }

    fun removePhoneNumber(id: Int){
        scope.launch {
            userRepo.removePhoneNumber(removePhoneNumberState, id)
        }
    }
}
