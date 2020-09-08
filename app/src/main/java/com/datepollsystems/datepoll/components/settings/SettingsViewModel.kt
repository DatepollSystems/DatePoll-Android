package com.datepollsystems.datepoll.components.settings

import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class SettingsViewModel : ViewModel(), KoinComponent {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = userRepo.user
    val phoneNumbers = userRepo.phoneNumbers
    val sessions = MutableLiveData<List<SessionModel>>()

    val updateUserState = MutableLiveData<ENetworkState>()
    val loadUserState = MutableLiveData<ENetworkState>()
    val removeAddPhoneNumberState = MutableLiveData<ENetworkState>()
    private val loadSessionsState = MutableLiveData<ENetworkState>()
    private val deleteSessionSate = MutableLiveData<ENetworkState>()
    val checkPasswordState = MutableLiveData<ENetworkState>()
    val changePasswordState = MutableLiveData<ENetworkState>()

    val changePasswordStep = MutableLiveData(EStep.ONE)
    val changePasswordOldPass = MutableLiveData("")
    val changePasswordNewPass = MutableLiveData("")
    val changePasswordConfirmNewPass = MutableLiveData("")

    val calendarSessionToken = MutableLiveData<String>()
    val calendarSessionTokenResetState = MutableLiveData<ENetworkState>()
    val calendarSessionTokenState = MutableLiveData<ENetworkState>()

    val shownInBirthdayList = MutableLiveData(true)
    val getShownInBirthdayListState = MutableLiveData<ENetworkState>()

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

                Timber.i("New List: ${list.toString()}")

                sessions.postValue(list!!)
            }
        }
    }

    fun checkPassword(password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val msg: Message? = userRepo.checkPassword(checkPasswordState, password)

            msg?.let {
                Timber.i(msg.msg)
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val msg: Message? =
                userRepo.changePassword(changePasswordState, oldPassword, newPassword)

            msg?.let {
                Timber.i(msg.msg)
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

    fun onBirthdayChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Timber.i("changed ${buttonView.id}")
        postIsBirthdayShown(isChecked, getShownInBirthdayListState)
    }

    fun checkIfBirthdayIsShown() {
        viewModelScope.launch(Dispatchers.Default) {
            val msg = userRepo.checkIfShownInBirthdayList(getShownInBirthdayListState)
            msg?.let {
                Timber.i(msg.toString())
                shownInBirthdayList.postValue(msg.settingsValue)
            }
        }
    }

    fun postIsBirthdayShown(v: Boolean, state: MutableLiveData<ENetworkState>) {
        viewModelScope.launch(Dispatchers.Default) {
            val msg = userRepo.postIsBirthdayShown(v, state)
            msg?.let {
                Timber.i(msg.toString())
                shownInBirthdayList.postValue(msg.settingsValue)
            }
        }
    }
}
