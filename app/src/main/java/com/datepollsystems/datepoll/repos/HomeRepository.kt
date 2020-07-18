package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.HomeScreen
import com.datepollsystems.datepoll.network.InstanceApi
import org.koin.core.inject

class HomeRepository: BaseRepository() {

    private val api: InstanceApi by inject()

    suspend fun loadHomepage(state: MutableLiveData<ENetworkState>): HomeScreen? {
        return apiCall(
            call = { api.getHomepage(prefs.jwt!!) },
            state = state
        )
    }
}
