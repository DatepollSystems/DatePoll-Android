package com.bke.datepoll.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.db.model.UserDbModel
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel: BaseViewModel() {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = MediatorLiveData<UserDbModel>()
    val userLoaded = MutableLiveData<LiveData<UserDbModel>>()
    val userUpdated = MutableLiveData<Boolean>()

    fun loadUserdata(){
        scope.launch {
            userLoaded.postValue(userRepo.loadUser(true))
        }
    }

    fun updateUser(u: UpdateUserRequest) {
        scope.launch {
            userRepo.updateUser(u)

            userUpdated.postValue(true)
        }
    }
}
