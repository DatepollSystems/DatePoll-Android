package com.bke.datepoll.repos

import com.bke.datepoll.network.DatepollApi
import com.bke.datepoll.data.requests.LogoutRequestModel
import com.bke.datepoll.data.requests.LogoutResponseModel
import com.bke.datepoll.database.DatepollDatabase
import okhttp3.ResponseBody
import org.koin.core.inject

class ServerRepository: BaseRepository("ServerRepository"){

    private val api: DatepollApi by inject()
    private val db: DatepollDatabase by inject()

    suspend fun isServiceOnline(): ResponseBody? {
        return safeApiCall(
            api,
            call = { api.checkIfServiceIsOnline() },
            errorMessage = "Service could not be reached"
        )
    }

    suspend fun logout(request: LogoutRequestModel): LogoutResponseModel? {
        //TODO Drop all DBs
        
        return safeApiCall(
            api,
            call = { api.logout(prefs.JWT!!, request) },
            errorMessage = "Could not perform logout"
        )
    }
}