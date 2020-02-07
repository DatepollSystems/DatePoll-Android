package com.bke.datepoll.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.model.NewPhoneNumberRequest
import com.bke.datepoll.data.model.SessionModel
import com.bke.datepoll.data.requests.Message
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel : BaseViewModel() {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = userRepo.user
    val phoneNumbers = userRepo.phoneNumbers
    val sessions = MutableLiveData<List<SessionModel>>()

    val updateUserState = MutableLiveData<ENetworkState>()
    val loadUserState = MutableLiveData<ENetworkState>()
    val removeAddPhoneNumberState = MutableLiveData<ENetworkState>()
    val loadSessionsState = MutableLiveData<ENetworkState>()
    val deleteSessionSate = MutableLiveData<ENetworkState>()


    fun loadUserdata() {
        scope.launch {
            userRepo.getUser(loadUserState, true)
        }
    }

    fun updateUser(u: UpdateUserRequest) {
        scope.launch {
            userRepo.updateUser(updateUserState, u)
        }
    }

    fun addPhoneNumber(p: NewPhoneNumberRequest) {
        scope.launch {
            userRepo.addPhoneNumber(removeAddPhoneNumberState, p)
        }
    }

    fun removePhoneNumber(id: Int) {
        scope.launch {
            userRepo.removePhoneNumber(removeAddPhoneNumberState, id)
        }
    }

    fun loadSessions() {
        scope.launch {
            val s = userRepo.loadSessions(loadSessionsState)

            sessions.postValue(s)
        }
    }

    fun deleteSession(item: SessionModel) {
        scope.launch {


            val msg: Message? = userRepo.deleteSession(
                deleteSessionSate,
                item
            )

            msg?.let {
                val list = sessions.value?.filterIndexed { _, sessionModel ->
                    sessionModel.id != item.id
                }

                Log.i("New List: ", list.toString())

                sessions.postValue(list)
            }
        }
    }
}
