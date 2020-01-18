package com.bke.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.data.model.NewPhoneNumberRequest
import com.bke.datepoll.data.model.NewPhoneNumberResponse
import com.bke.datepoll.data.model.UserLiveDataElements
import com.bke.datepoll.data.model.UserModel
import com.bke.datepoll.data.requests.UpdateUserRequest
import com.bke.datepoll.database.DatepollDatabase
import com.bke.datepoll.database.dao.*
import com.bke.datepoll.database.model.EmailAddressDbModel
import com.bke.datepoll.database.model.PerformanceBadgesDbModel
import com.bke.datepoll.database.model.PermissionDbModel
import com.bke.datepoll.network.DatepollApi
import org.koin.core.inject
import java.util.*

class UserRepository : BaseRepository("UserRepository") {

    private val api: DatepollApi by inject()
    private val db: DatepollDatabase by inject()

    private val userDao: UserDao = db.userDao()
    private val phoneNumberDao: PhoneNumberDao = db.phoneDao()
    private val emailDao: EmailDao = db.emailDao()
    private val performanceBadgesDao: PerformanceBadgesDao = db.performanceBadgesDao()
    private val permissionsDao: PermissionsDao = db.permissionDao()

    val user = userDao.getUser()
    val phoneNumbers = phoneNumberDao.getPhoneNumbers()

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
            loadUserFromServer(state)?.let {
                userDao.addUser(it.getUserDbModelPart())
                // TODO update also child tables!!!
                phoneNumberDao.saveSetOfPhoneNumbers(it.phone_numbers)
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
            phoneNumberDao.savePhoneNumer(it.phoneNumber)
        }
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

        val performanceBadgesToStore = ArrayList<PerformanceBadgesDbModel>()
        val emailsToStore = ArrayList<EmailAddressDbModel>()
        val permissionsToStore = ArrayList<PermissionDbModel>()

        if (user.phone_numbers.isNullOrEmpty())
            phoneNumberDao.saveSetOfPhoneNumbers(user.phone_numbers)

        if (user.email_addresses.isNotEmpty()) {

            user.email_addresses.forEach {
                emailsToStore.add(EmailAddressDbModel(0, it, user.id))
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