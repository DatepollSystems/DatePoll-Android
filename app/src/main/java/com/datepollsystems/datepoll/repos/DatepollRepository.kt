package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.Instances
import com.datepollsystems.datepoll.network.DatepollApi
import org.koin.core.component.inject

class DatepollRepository: BaseRepository() {

    val api: DatepollApi by inject()

    suspend fun loadInstances(ld: MutableLiveData<ENetworkState>): Instances? {
        return apiCall(
            call = { api.getInstances() },
            state = ld
        )
    }
}