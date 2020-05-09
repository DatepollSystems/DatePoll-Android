package com.datepollsystems.datepoll.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.data.NewPhoneNumberRequest
import com.datepollsystems.datepoll.data.SessionModel
import com.datepollsystems.datepoll.data.Message
import com.datepollsystems.datepoll.data.MessageToken
import com.datepollsystems.datepoll.data.UpdateUserRequest
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.repos.UserRepository
import com.datepollsystems.datepoll.ui.settings.EStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class SettingsViewModel : BaseViewModel() {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()
    private val tag = "SettingsViewModel"

    val user = userRepo.user
    val phoneNumbers = userRepo.phoneNumbers
    val sessions = MutableLiveData<List<SessionModel>>()

    val updateUserState = MutableLiveData<ENetworkState>()
    val loadUserState = MutableLiveData<ENetworkState>()
    val removeAddPhoneNumberState = MutableLiveData<ENetworkState>()
    val loadSessionsState = MutableLiveData<ENetworkState>()
    val deleteSessionSate = MutableLiveData<ENetworkState>()
    val checkPasswordState = MutableLiveData<ENetworkState>()
    val changePasswordState = MutableLiveData<ENetworkState>()

    val changePasswordStep = MutableLiveData<EStep>(EStep.ONE)
    val changePasswordOldPass = MutableLiveData<String>("")
    val changePasswordNewPass = MutableLiveData<String>("")
    val changePasswordConfirmNewPass = MutableLiveData<String>("")

    val calendarSessionToken = MutableLiveData<String>()
    val calendarSessionTokenResetState = MutableLiveData<ENetworkState>()
    val calendarSessionTokenState = MutableLiveData<ENetworkState>()

    fun loadUserdata() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.getUser(loadUserState, true)
        }
    }

    fun updateUser(u: UpdateUserRequest) {
        viewModelScope.launch(Dispatchers.Default) {
            userRepo.updateUser(updateUserState, u)
        }
    }

    fun addPhoneNumber(p: NewPhoneNumberRequest) {
        viewModelScope.launch(Dispatchers.Default) {
            userRepo.addPhoneNumber(removeAddPhoneNumberState, p)
        }
    }

    fun removePhoneNumber(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            userRepo.removePhoneNumber(removeAddPhoneNumberState, id)
        }
    }

    fun loadSessions() {
        viewModelScope.launch(Dispatchers.Default) {
            val s = userRepo.loadSessions(loadSessionsState)

            sessions.postValue(s)
        }
    }

    fun deleteSession(item: SessionModel) {
        viewModelScope.launch(Dispatchers.Default) {

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

    fun checkPassword(password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val msg: Message? = userRepo.checkPassword(checkPasswordState, password)

            msg?.let {
                Log.i(tag, msg.msg)
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val msg: Message? =
                userRepo.changePassword(changePasswordState, oldPassword, newPassword)

            msg?.let {
                Log.i(tag, msg.msg)
            }
        }
    }

    fun resetCalendarToken() {
        viewModelScope.launch(Dispatchers.Default) {
            val msg: MessageToken? = userRepo.resetCalendarToken(calendarSessionTokenResetState)

            calendarSessionToken.postValue("${prefs.serverAddress}:${prefs.serverPort}/api/user/calendar/${msg?.token}")
        }
    }

    fun getCalendarToken() {
        viewModelScope.launch(Dispatchers.Default) {
            val msg: MessageToken? = userRepo.getCalendarToken(calendarSessionTokenState)
            msg?.let {
                calendarSessionToken.postValue(
                    "${prefs.serverAddress}:${prefs.serverPort}/api/user/calendar/${it.token}"
                )
            }
        }
    }
}
