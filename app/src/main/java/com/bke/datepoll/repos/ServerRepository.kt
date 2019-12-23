package com.bke.datepoll.repos

import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.data.requests.LogoutRequestModel
import com.bke.datepoll.data.requests.LogoutResponseModel
import com.bke.datepoll.db.DatepollDatabase
import okhttp3.ResponseBody

class ServerRepository(
    private val api: DatepollApi,
    private val db: DatepollDatabase
) : BaseRepository("ServerRepository"){

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
            call = { api.logout(request) },
            errorMessage = "Could not perform logout"
        )
    }
}