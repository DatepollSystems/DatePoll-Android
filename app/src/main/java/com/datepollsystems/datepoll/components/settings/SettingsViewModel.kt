package com.datepollsystems.datepoll.components.settings

import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.repos.UserRepository
import com.datepollsystems.datepoll.utils.formatDateToEn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class SettingsViewModel : ViewModel(), KoinComponent {

    private val prefs: Prefs by inject()
    private val userRepo: UserRepository by inject()

    val user = userRepo.user
    val phoneNumbers = userRepo.phoneNumbers
    val sessions = MutableLiveData<List<SessionModel>>()

    val updateUserState = MutableLiveData<ENetworkState?>()
    val loadUserState = MutableLiveData<ENetworkState?>()
    val removeAddPhoneNumberState = MutableLiveData<ENetworkState?>()
    private val loadSessionsState = MutableLiveData<ENetworkState?>()
    private val deleteSessionSate = MutableLiveData<ENetworkState?>()
    val checkPasswordState = MutableLiveData<ENetworkState?>()
    val changePasswordState = MutableLiveData<ENetworkState?>()
    val showBirthdayPicker = MutableLiveData<Boolean?>(false)

    val changePasswordStep = MutableLiveData(EStep.ONE)
    val changePasswordOldPass = MutableLiveData("")
    val changePasswordNewPass = MutableLiveData("")
    val changePasswordConfirmNewPass = MutableLiveData("")

    val calendarSessionToken = MutableLiveData<String>()
    val calendarSessionTokenResetState = MutableLiveData<ENetworkState?>()
    val calendarSessionTokenState = MutableLiveData<ENetworkState?>()

    val shownInBirthdayList = MutableLiveData(true)
    val getShownInBirthdayListState = MutableLiveData<ENetworkState?>()

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

    fun launchBirthdayPicker(){
        showBirthdayPicker.postValue(true)
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

    fun updateBirthday(long: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val u = user.value!!
            val updateUserRequest = UpdateUserRequest(
                title = u.title.toString(),
                firstname = u.firstname.toString(),
                surname = u.surname.toString(),
                birthday = formatDateToEn(Date(long)),
                location = u.location.toString(),
                streetname = u.streetname.toString(),
                streetnumber = u.streetnumber.toString(),
                zipcode = u.zipcode!!.toInt()
            )
            Timber.i(updateUserRequest.toString())
            updateUser(updateUserRequest)
        }
    }

    fun postIsBirthdayShown(v: Boolean, state: MutableLiveData<ENetworkState?>) {
        viewModelScope.launch(Dispatchers.Default) {
            val msg = userRepo.postIsBirthdayShown(v, state)
            msg?.let {
                Timber.i(msg.toString())
                shownInBirthdayList.postValue(msg.settingsValue)
            }
        }
    }
}
