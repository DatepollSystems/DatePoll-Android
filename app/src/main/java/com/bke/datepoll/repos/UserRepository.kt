package com.bke.datepoll.repos

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.data.model.*
import com.bke.datepoll.data.requests.ChangePasswordRequestModel
import com.bke.datepoll.data.requests.Message
import com.bke.datepoll.data.requests.PasswordRequestModel
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.database.DatepollDatabase
import com.bke.datepoll.database.dao.*
import com.bke.datepoll.database.model.EmailAddressDbModel
import com.bke.datepoll.database.model.PerformanceBadgesDbModel
import com.bke.datepoll.database.model.PermissionDbModel
import com.bke.datepoll.network.DatepollApi
import org.koin.core.inject
import org.koin.core.logger.MESSAGE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserRepository : BaseRepository("UserRepository") {

    private val api: DatepollApi by inject()
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
            storeUser(it).user
        }
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
            call = { api.addPhoneNumber(prefs.JWT!!, p) },
            state = state
        )

        result?.let {
            phoneNumberDao.savePhoneNumber(it.phoneNumber)
        }
    }

    suspend fun removePhoneNumber(state: MutableLiveData<ENetworkState>, id: Int) {
        val result = apiCall(
            call = { api.removePhoneNumber(id, prefs.JWT!!) },
            state = state
        )

        if (result != null) {
            phoneNumberDao.deletePhoneNumber(id.toLong())
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
            call = { api.addEmail(prefs.JWT!!, AddEmailRequest(emails = emails)) },
            state = state
        )

        result?.let {
            storeUser(it.user)
        }
    }

    fun addEmail(e: String) {
        emailDao.addEmail(EmailAddressDbModel(email = e, userId = user.value!!.id))
    }

    fun removeEmail(e: String) {
        emailDao.deleteEmail(e)
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun loadSessions(state: MutableLiveData<ENetworkState>): List<SessionModel> {
        val result = apiCall(
            call = { api.getSessions(prefs.JWT!!) },
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
                val se = SessionModel(item.id, item.information, newDate, item.deleteSession)
                s.add(se)
            }
        }


        return s
    }

    suspend fun deleteSession(state: MutableLiveData<ENetworkState>, item: SessionModel): Message? {
        return apiCall(
            state = state,
            call = { api.deleteSession(item.id, prefs.JWT!!) }
        )
    }

    suspend fun checkPassword(state: MutableLiveData<ENetworkState>, password: String): Message? {
        return apiCall(
            state = state,
            call = { api.checkOldPassword(prefs.JWT!!, PasswordRequestModel(password)) }
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
                    prefs.JWT!!,
                    ChangePasswordRequestModel(oldPassword, newPassword)
                )
            },
            state = state
        )
    }

    private suspend fun updateUserOnServer(
        state: MutableLiveData<ENetworkState>,
        user: UpdateUserRequest
    ): UserModel? {
        return apiCall(
            call = { api.updateCurrentUser(prefs.JWT!!, user) },
            state = state
        )?.user
    }

    private suspend fun loadUserFromServer(state: MutableLiveData<ENetworkState>): UserModel? {
        return apiCall(
            call = { api.currentUser(prefs.JWT!!) },
            state = state
        )?.user
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
                emailsToStore.add(EmailAddressDbModel(it, user.id))
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
                permissionsToStore.add(PermissionDbModel(0, it, user.id))
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