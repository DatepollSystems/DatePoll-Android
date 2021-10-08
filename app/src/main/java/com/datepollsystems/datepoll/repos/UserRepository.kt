package com.datepollsystems.datepoll.repos

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.db.DatepollDatabase
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.EmailAddressDbModel
import com.datepollsystems.datepoll.data.PerformanceBadgesDbModel
import com.datepollsystems.datepoll.data.PermissionDbModel
import com.datepollsystems.datepoll.db.dao.*
import com.datepollsystems.datepoll.network.InstanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.inject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserRepository : BaseRepository() {

    private val api: InstanceApi by inject()
    private val db: DatepollDatabase by inject()

    private val userDao: UserDao = db.userDao()
    private val phoneNumberDao: PhoneNumberDao = db.phoneDao()
    private val emailDao: EmailDao = db.emailDao()
    private val performanceBadgesDao: PerformanceBadgesDao = db.performanceBadgesDao()
    private val permissionsDao: PermissionsDao = db.permissionDao()

    val user = userDao.getUser()
    val phoneNumbers by lazy { phoneNumberDao.getPhoneNumbers() }
    val emails by lazy { emailDao.getEmails() }

    suspend fun updateUser(state: MutableLiveData<ENetworkState>, user: UpdateUserRequest) {
        updateUserOnServer(state, user)?.let {
            withContext(Dispatchers.IO) {
                storeUser(it).user
            }
        }
    }

    suspend fun checkIfShownInBirthdayList(state: MutableLiveData<ENetworkState>): ShownInBirthdayListResponse? {
        return apiCall(
            call = { api.getIfUserIsShownInBirthdayList(prefs.jwt!!) },
            state = state
        )
    }

    suspend fun postIsBirthdayShown(checked: Boolean, state: MutableLiveData<ENetworkState>): ShownInBirthdayListResponse? {
        val r = PostShownInBirthdayListRequest(checked)

        return apiCall(
            call = { api.postIfUserIsShownIBirthdayList(prefs.jwt!!, r)},
            state = state
        )
    }

    suspend fun getUser(state: MutableLiveData<ENetworkState>, force: Boolean = false) {
        if (db.userDao().getCount() != 1L) {
            loadUserFromServer(state)?.let {
                storeUser(it)
            }

            return
        }

        val savedAt = userDao.getSavedAt()
        if ((Date().time - savedAt) > 3600000 || force) {
            /**
             * user is older then 1 hour -> reload user from server
             */
            loadUserFromServer(state)?.let { u ->
                userDao.addUser(u.getUserDbModelPart())
                //TODO update also child tables!!!
                phoneNumberDao.saveSetOfPhoneNumbers(u.phone_numbers)
                val email = ArrayList<EmailAddressDbModel>()

                u.email_addresses.forEach {
                    email.add(
                        EmailAddressDbModel(
                            email = it,
                            userId = u.id
                        )
                    )
                }
                emailDao.addEmails(email)


                //performanceBadgesDao.addPerformanceBadges()
                state.postValue(ENetworkState.DONE)
            }
        }
    }

    suspend fun addPhoneNumber(state: MutableLiveData<ENetworkState>, p: NewPhoneNumberRequest) {
        val result = apiCall(
            call = { api.addPhoneNumber(prefs.jwt!!, p) },
            state = state
        )

        result?.let {
            withContext(Dispatchers.IO) {
                phoneNumberDao.savePhoneNumber(it.phoneNumber)
            }
        }
    }

    suspend fun removePhoneNumber(state: MutableLiveData<ENetworkState>, id: Int) {
        val result = apiCall(
            call = { api.removePhoneNumber(id, prefs.jwt!!) },
            state = state
        )

        if (result != null) {
            withContext(Dispatchers.IO) {
                phoneNumberDao.deletePhoneNumber(id.toLong())
            }
        } else {
            state.postValue(ENetworkState.ERROR)
        }
    }

    suspend fun saveEmailsToServer(state: MutableLiveData<ENetworkState>) {
        val emails = ArrayList<String>()
        for (e in this.emails.value!!.iterator()) {
            emails.add(e.email)
        }

        val result = apiCall(
            call = {
                api.addEmail(
                    prefs.jwt!!,
                    AddEmailRequest(emails = emails)
                )
            },
            state = state
        )

        result?.let {
            withContext(Dispatchers.IO) {
                storeUser(it.user)
            }
        }
    }

    fun addEmail(e: String) {
        emailDao.addEmail(
            EmailAddressDbModel(
                email = e,
                userId = user.value!!.id
            )
        )
    }

    fun removeEmail(e: String) {
        emailDao.deleteEmail(e)
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun loadSessions(state: MutableLiveData<ENetworkState>): List<SessionModel> {
        val result = apiCall(
            call = { api.getSessions(prefs.jwt!!) },
            state = state
        )

        val s = ArrayList<SessionModel>()
        val serverPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        val uiPattern = "dd.MM.yyyy HH:mm:ss"
        val formatter = SimpleDateFormat(serverPattern)
        val uiFormatter = SimpleDateFormat(uiPattern)

        result?.let {
            for (item in it.sessions) {
                val oldDate = formatter.parse(item.lastUsed)

                val newDate = uiFormatter.format(oldDate!!)
                val se = SessionModel(
                    item.id,
                    item.information,
                    newDate
                )
                s.add(se)
            }
        }


        return s
    }

    suspend fun deleteSession(state: MutableLiveData<ENetworkState>, item: SessionModel): Message? {
        return apiCall(
            state = state,
            call = { api.deleteSession(item.id, prefs.jwt!!) }
        )
    }

    suspend fun checkPassword(state: MutableLiveData<ENetworkState>, password: String): Message? {
        return apiCall(
            state = state,
            call = {
                api.checkOldPassword(
                    prefs.jwt!!,
                    PasswordRequestModel(password)
                )
            }
        )
    }

    suspend fun changePassword(
        state: MutableLiveData<ENetworkState>,
        oldPassword: String,
        newPassword: String
    ): Message? {
        return apiCall(
            call = {
                api.changeOldPassword(
                    prefs.jwt!!,
                    ChangePasswordRequestModel(
                        oldPassword,
                        newPassword
                    )
                )
            },
            state = state
        )
    }

    suspend fun getCalendarToken(state: MutableLiveData<ENetworkState>): MessageToken? {
        return apiCall(
            call = { api.getCalendarToken(prefs.jwt!!) },
            state = state
        )
    }

    suspend fun resetCalendarToken(state: MutableLiveData<ENetworkState>): MessageToken? {
        val otherState = MutableLiveData<ENetworkState>()

        val delete = apiCall(state = otherState, call = { api.deleteCalendarToken(prefs.jwt!!) })

        if (otherState.value!! == ENetworkState.ERROR) {
            state.postValue(ENetworkState.ERROR)
            return null
        } else
            Log.i("UserRepository", delete!!.msg)

        return getCalendarToken(state)
    }

    private suspend fun updateUserOnServer(
        state: MutableLiveData<ENetworkState>,
        user: UpdateUserRequest
    ): UserModel? {
        return apiCall(
            call = { api.updateCurrentUser(prefs.jwt!!, user) },
            state = state
        )?.user
    }

    private suspend fun loadUserFromServer(state: MutableLiveData<ENetworkState>): UserModel? {

        Timber.i(prefs.jwt)
        var model = apiCall(
            call = { api.currentUser(prefs.jwt!!) },
            state = state
        )

        return model?.user
    }

    private fun storeUser(user: UserModel): UserLiveDataElements {

        userDao.addUser(user.getUserDbModelPart())

        val performanceBadgesToStore = ArrayList<PerformanceBadgesDbModel>()
        val emailsToStore = ArrayList<EmailAddressDbModel>()
        val permissionsToStore = ArrayList<PermissionDbModel>()

        if (user.phone_numbers.isNullOrEmpty())
            phoneNumberDao.saveSetOfPhoneNumbers(user.phone_numbers)

        if (user.email_addresses.isNotEmpty()) {

            user.email_addresses.forEach {
                emailsToStore.add(
                    EmailAddressDbModel(
                        it,
                        user.id
                    )
                )
            }

            emailDao.addEmails(emailsToStore)
        }

        if (user.performance_badges.isNotEmpty()) {

            user.performance_badges.forEach {
                performanceBadgesToStore.add(it.getPerformanceBadgesDbModel(user.id))
            }

            performanceBadgesDao.addPerformanceBadges(performanceBadgesToStore)
        }

        if (user.permissions.isNotEmpty()) {
            user.permissions.forEach {
                permissionsToStore.add(
                    PermissionDbModel(
                        0,
                        it,
                        user.id
                    )
                )
            }

            permissionsDao.addPermissions(permissionsToStore)
        }


        return UserLiveDataElements(
            user = userDao.getUserById(user.id),
            phoneNumbers = phoneNumberDao.getPhoneNumbers(),
            permissions = permissionsDao.getAllPermissionsByUserId(user.id),
            performanceBadges = performanceBadgesDao.getPerformanceBadgesByUserId(user.id),
            emailAddress = emailDao.getEmailsOfUser(user.id)
        )
    }
}