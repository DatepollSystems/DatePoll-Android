package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.HomeScreen
import com.datepollsystems.datepoll.network.DatepollApi
import org.koin.core.inject

class HomeRepository: BaseRepository("HomeRepository") {

    private val api: DatepollApi by inject()

    suspend fun loadHomepage(state: MutableLiveData<ENetworkState>): HomeScreen? {
        return apiCall(
            call = { api.getHomepage(prefs.jwt!!) },
            state = state
        )
    }
}
