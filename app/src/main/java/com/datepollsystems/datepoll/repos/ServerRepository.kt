package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.Instances
import com.datepollsystems.datepoll.network.DatepollApi
import com.datepollsystems.datepoll.data.LogoutRequestModel
import com.datepollsystems.datepoll.data.LogoutResponseModel
import okhttp3.ResponseBody
import org.koin.core.inject

class ServerRepository: BaseRepository("ServerRepository"){

    private val api: DatepollApi by inject()


    suspend fun isServiceOnline(state: MutableLiveData<ENetworkState>): ResponseBody? {
        return apiCall(
            call = { api.checkIfServiceIsOnline() },
            state = state
        )
    }

    suspend fun logout(request: LogoutRequestModel): LogoutResponseModel? {
        //TODO Drop all DBs
        
        return apiCall(
            call = { api.logout(prefs.jwt!!, request) },
            state = MutableLiveData()
        )
    }


    suspend fun loadInstances(ld: MutableLiveData<ENetworkState>): Instances? {
        return apiCall(
            call = { api.getInstances() },
            state = ld
        )
    }
}