package com.datepollsystems.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.data.Instance
import com.datepollsystems.datepoll.repos.ENetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject


class ServerInputViewModel : BaseViewModel() {

    val serverAddress = MutableLiveData<String>()
    val serverPort = MutableLiveData<Int>(9230)
    val instanceMenu = MutableLiveData<List<Instance>>()
    val instanceMenuState = MutableLiveData<ENetworkState>()
    val instanceClickResult = MutableLiveData<Instance>()
    val validateInstanceState = MutableLiveData<ENetworkState>()


    private val serverRepo: DatepollRepository by inject()

    fun loadInstances(){
        viewModelScope.launch(Dispatchers.Default) {
            val l = serverRepo.loadInstances(instanceMenuState)
            instanceMenu.postValue(l?.instances)
        }
    }
}