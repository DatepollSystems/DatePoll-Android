package com.datepollsystems.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.repos.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject


class ServerInputViewModel : BaseViewModel() {
    val serverAddress = MutableLiveData<String>()
    val serverPort = MutableLiveData<Int>(9230)

    val validateInstanceState = MutableLiveData<ENetworkState>()

    private val serverRepo: ServerRepository by inject()

    fun validateInstance(url: String) {
        viewModelScope.launch(Dispatchers.Default) {
            serverRepo.isServiceOnline(validateInstanceState)
        }
    }
}