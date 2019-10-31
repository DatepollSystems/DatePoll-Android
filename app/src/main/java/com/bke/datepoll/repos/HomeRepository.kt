package com.bke.datepoll.repos

import android.util.Log
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.data.requests.CurrentUserResponseModel
import com.bke.datepoll.data.requests.RefreshTokenWithSessionRequest
import com.bke.datepoll.data.requests.RefreshTokenWithSessionResponse
import com.bke.datepoll.db.UserDao
import com.bke.datepoll.prefs
import okhttp3.ResponseBody

class HomeRepository(private val api: DatepollApi, private val dao: UserDao) : BaseRepository() {

    suspend fun getCurrentUser(): CurrentUserResponseModel? {
        return safeApiCall(
            api,
            call = { api.currentUser(prefs.JWT!!) },
            errorMessage = "Could not get current user"
        )
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