package com.bke.datepoll.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.requests.LogoutRequestModel
import com.bke.datepoll.data.requests.LogoutResponseModel
import com.bke.datepoll.db.model.PermissionDbModel
import com.bke.datepoll.db.model.UserDbModel
import com.bke.datepoll.repos.ServerRepository
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch


class MainViewModel(
    private val prefs: Prefs,
    private val userRepository: UserRepository,
    private val serverRepository: ServerRepository
) : BaseViewModel() {

    private val tag = "MainViewModel"

    val loaded = MutableLiveData<LiveData<UserDbModel>>()
    val user = MediatorLiveData<UserDbModel>()
    val logout = MutableLiveData<Boolean>(false)
    val permissions = MutableLiveData<List<PermissionDbModel>>()

    fun loadUserData() {
       scope.launch {
           val loadedUser = userRepository.loadUser(true)
           loaded.postValue(loadedUser)
       }
    }

    fun logout(){
        scope.launch {
            Log.i(tag, "start logout process")

            val session = prefs.SESSION!!
            val response: LogoutResponseModel? =
                serverRepository.logout(LogoutRequestModel(session_token = session))

            if(response != null && response.username.isNotBlank()){
                Log.i(tag, "logout successful")
            }

            prefs.SESSION = ""
            prefs.JWT = ""
            prefs.IS_LOGGED_IN = false
            logout.postValue(true)
        }
    }
}