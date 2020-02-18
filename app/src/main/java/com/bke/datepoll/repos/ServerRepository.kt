package com.bke.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.network.DatepollApi
import com.bke.datepoll.data.requests.LogoutRequestModel
import com.bke.datepoll.data.requests.LogoutResponseModel
import com.bke.datepoll.database.DatepollDatabase
import okhttp3.ResponseBody
import org.koin.core.inject

class ServerRepository: BaseRepository("ServerRepository"){

    private val api: DatepollApi by inject()


    suspend fun isServiceOnline(): ResponseBody? {
        return apiCall(
            call = { api.checkIfServiceIsOnline() },
            state = MutableLiveData()
        )
    }

    suspend fun logout(request: LogoutRequestModel): LogoutResponseModel? {
        //TODO Drop all DBs
        
        return apiCall(
            call = { api.logout(prefs.JWT!!, request) },
            state = MutableLiveData()
        )
    }
}