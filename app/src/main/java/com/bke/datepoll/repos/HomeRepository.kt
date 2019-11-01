package com.bke.datepoll.repos

import android.util.Log
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.data.model.UserLiveDataElements
import com.bke.datepoll.data.requests.CurrentUserResponseModel
import com.bke.datepoll.data.requests.RefreshTokenWithSessionRequest
import com.bke.datepoll.data.requests.RefreshTokenWithSessionResponse
import com.bke.datepoll.db.dao.*
import com.bke.datepoll.db.model.*
import com.bke.datepoll.prefs
import okhttp3.ResponseBody

class HomeRepository(
    private val api: DatepollApi,
    private val userDao: UserDao,
    private val phoneNumberDao: PhoneNumberDao,
    private val emailDao: EmailDao,
    private val performanceBadgesDao: PerformanceBadgesDao,
    private val permissionsDao: PermissionsDao) : BaseRepository() {

    suspend fun getCurrentUserAndStoreIt(): UserLiveDataElements {
        val current: CurrentUserResponseModel? = safeApiCall(
            api,
            call = { api.currentUser(prefs.JWT!!) },
            errorMessage = "Could not get current user"
        )

        //Store user
        val user = current!!.user
        val userId = userDao.addUser(user.getUserDbModelPart())

        val performanceBadgesToStore = ArrayList<PerformanceBadgesDbModel>()
        val emailsToStore = ArrayList<EmailAddressDbModel>()
        val permissionsToStore = ArrayList<PermissionDbModel>()

        if(user.phone_numbers.isNullOrEmpty())
            phoneNumberDao.saveSetOfPhoneNumbers(user.phone_numbers)

        if(user.email_addresses.isNotEmpty()){

            user.email_addresses.forEach {
                emailsToStore.add(EmailAddressDbModel(0, it, user.id))
            }

            emailDao.addEmails(emailsToStore)
        }

        if(user.performance_badges.isNotEmpty()){

            user.performance_badges.forEach {
                performanceBadgesToStore.add(it.getPerformanceBadgesDbModel(user.id))
            }

            performanceBadgesDao.addPerformanceBadges(performanceBadgesToStore)
        }

        if(user.permissions.isNotEmpty()){
            user.permissions.forEach {
                permissionsToStore.add(PermissionDbModel(0, it, user.id))
            }

            permissionsDao.addPermissions(permissionsToStore)
        }


        return UserLiveDataElements(
            user = userDao.getUserById(user.id),
            phoneNumbers = phoneNumberDao.getPhoneNumbersForUser(user.id),
            permissions = permissionsDao.getAllPermissionsByUserId(user.id),
            performanceBadges = performanceBadgesDao.getPerformanceBadgesByUserId(user.id),
            emailAddress = emailDao.getEmailsOfUser(user.id)
        )

    }

    fun updateUser(user: UserDbModel){
        userDao.addUser(user)
    }

    suspend fun isServiceOnline(): ResponseBody? {
        return safeApiCall(
            api,
            call = { api.checkIfServiceIsOnline() },
            errorMessage = "Service could not be reached"
        )
    }

    suspend fun renewToken(){
        val jwtRefreshRequest = RefreshTokenWithSessionRequest(prefs.SESSION!!)

        val response: RefreshTokenWithSessionResponse? = safeApiCall(
            api,
            call = { api.refreshTokenWithSession(jwtRefreshRequest)},
            errorMessage = "Could not renew token"
        )

        if(response != null) {
            prefs.JWT = response.token
            Log.i("Refreshed jwt token", response.msg)
        }
    }


}