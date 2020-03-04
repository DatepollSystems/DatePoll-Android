package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.ServerRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class ServerInputViewModel : BaseViewModel(){
    val serverAddress = MutableLiveData<String>()
    val serverPort = MutableLiveData<Int>(9230)

    val validateInstanceState = MutableLiveData<ENetworkState>()

    private val serverRepo: ServerRepository by inject()

    fun validateInstance(url: String) {
        scope.launch {
            serverRepo.isServiceOnline(validateInstanceState)
        }
    }
}