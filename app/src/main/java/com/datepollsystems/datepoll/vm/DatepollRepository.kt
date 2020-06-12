package com.datepollsystems.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.Instances
import com.datepollsystems.datepoll.network.DatepollApi
import com.datepollsystems.datepoll.repos.BaseRepository
import com.datepollsystems.datepoll.repos.ENetworkState
import org.koin.core.inject

class DatepollRepository: BaseRepository() {

    val api: DatepollApi by inject()

    suspend fun loadInstances(ld: MutableLiveData<ENetworkState>): Instances? {
        return apiCall(
            call = { api.getInstances() },
            state = ld
        )
    }
}