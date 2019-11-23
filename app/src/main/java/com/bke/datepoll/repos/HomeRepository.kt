package com.bke.datepoll.repos

import android.util.Log
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.data.model.UserLiveDataElements
import com.bke.datepoll.data.requests.*
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.db.dao.*
import com.bke.datepoll.db.model.EmailAddressDbModel
import com.bke.datepoll.db.model.PerformanceBadgesDbModel
import com.bke.datepoll.db.model.PermissionDbModel
import com.bke.datepoll.db.model.UserDbModel
import okhttp3.ResponseBody

//TODO change constructor to DI
class HomeRepository(
    private val api: DatepollApi,
    private val db: DatepollDatabase
    ) : BaseRepository("HomeRepository") {


    private val userDao: UserDao = db.userDao()
    private val phoneNumberDao: PhoneNumberDao = db.phoneDao()
    private val emailDao: EmailDao = db.emailDao()
    private val performanceBadgesDao: PerformanceBadgesDao = db.performanceBadgesDao()
    private val permissionsDao: PermissionsDao = db.permissionDao()

    suspend fun getCurrentUser(): CurrentUserResponseModel? {
        return safeApiCall(
            api,
            call = { api.currentUser(prefs.JWT!!) },
            errorMessage = "Could not get current user"
        )
    }

    suspend fun logout(request: LogoutRequestModel): LogoutResponseModel? {
        return safeApiCall(
            api,
            call = { api.logout(request) },
            errorMessage = "Could not perform logout"
        )
    }

    fun storeUser(current: CurrentUserResponseModel?): UserLiveDataElements{
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