package com.bke.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.data.model.HomeScreen
import com.bke.datepoll.network.DatepollApi
import org.koin.core.inject

class HomeRepository: BaseRepository("HomeRepository") {

    private val api: DatepollApi by inject()

    suspend fun loadHomepage(state: MutableLiveData<ENetworkState>): HomeScreen? {
        return apiCall(
            call = { api.getHomepage(prefs.JWT!!) },
            state = state
        )
    }
}
