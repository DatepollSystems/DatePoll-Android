package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.data.LogoutRequestModel
import com.datepollsystems.datepoll.data.LogoutResponseModel
import com.datepollsystems.datepoll.network.InstanceApi
import org.koin.core.inject

class ServerRepository: BaseRepository(){

    private val api: InstanceApi by inject()

    suspend fun logout(request: LogoutRequestModel): LogoutResponseModel? {
        //TODO Drop all DBs
        
        return apiCall(
            call = { api.logout(prefs.jwt!!, request) },
            state = MutableLiveData()
        )
    }



}