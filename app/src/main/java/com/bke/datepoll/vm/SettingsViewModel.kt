package com.bke.datepoll.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.NewPhoneNumberRequest
import com.bke.datepoll.data.SessionModel
import com.bke.datepoll.data.Message
import com.bke.datepoll.data.MessageToken
import com.bke.datepoll.data.UpdateUserRequest
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.UserRepository
import com.bke.datepoll.ui.settings.EStep
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

    fun checkPassword(password: String) {
        scope.launch {
            val msg: Message? = userRepo.checkPassword(checkPasswordState, password)

            msg?.let {
                Log.i(tag, msg.msg)
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        scope.launch {
            val msg: Message? = userRepo.changePassword(changePasswordState, oldPassword, newPassword)

            msg?.let {
                Log.i(tag, msg.msg)
            }
        }
    }

    fun resetCalendarToken(){
        scope.launch {
            val msg: MessageToken? = userRepo.resetCalendarToken(calendarSessionTokenResetState)

            calendarSessionToken.postValue("${prefs.SERVER_ADDRESS}:${prefs.SERVER_PORT}/api/user/calendar/${msg?.token}")
        }
    }

    fun getCalendarToken(){
        scope.launch {
            val msg: MessageToken? = userRepo.getCalendarToken(calendarSessionTokenState)
            msg?.let {
                calendarSessionToken.postValue(
                    "${prefs.SERVER_ADDRESS}:${prefs.SERVER_PORT}/api/user/calendar/${it.token}"
                )
            }
        }
    }
}
