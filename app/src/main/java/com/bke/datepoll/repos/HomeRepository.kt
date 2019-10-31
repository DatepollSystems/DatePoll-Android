package com.bke.datepoll.repos

import android.util.Log
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.db.model.EmailAddressDbModel
import com.bke.datepoll.db.model.PerformanceBadgesDbModel
import com.bke.datepoll.data.model.UserLiveDataElements
import com.bke.datepoll.data.requests.CurrentUserResponseModel
import com.bke.datepoll.data.requests.RefreshTokenWithSessionRequest
import com.bke.datepoll.data.requests.RefreshTokenWithSessionResponse
import com.bke.datepoll.db.PhoneNumberDao
import com.bke.datepoll.db.UserDao
import com.bke.datepoll.prefs
import okhttp3.ResponseBody

class HomeRepository(private val api: DatepollApi, private val userDao: UserDao, private val phoneNumberDao: PhoneNumberDao) : BaseRepository() {

    suspend fun getCurrentUserAndStoreIt(): UserLiveDataElements? {
        val current: CurrentUserResponseModel? = safeApiCall(
            api,
            call = { api.currentUser(prefs.JWT!!) },
            errorMessage = "Could not get current user"
        )

        //Store user
        val user = current!!.user
        userDao.addUser(user.getUserDbModelPart())

        if(user.phone_numbers.isNullOrEmpty())
            phoneNumberDao.saveSetOfPhoneNumbers(user.phone_numbers)

        if(user.email_addresses.isNotEmpty()){
            val emailsToStore = ArrayList<EmailAddressDbModel>()
            user.email_addresses.forEach {
                emailsToStore.add(EmailAddressDbModel(0, it, user.id))
            }

            //Store email addresses in DB TODO create dao
        }

        if(user.performance_badges.isNotEmpty()){
            val performanceBadgesToStore = ArrayList<PerformanceBadgesDbModel>()
            user.performance_badges.forEach {
                performanceBadgesToStore.add(it.getPerformanceBadgesDbModel(user.id))
            }

            //Store performance_badges in DB TODO create dao
        }






        return null

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