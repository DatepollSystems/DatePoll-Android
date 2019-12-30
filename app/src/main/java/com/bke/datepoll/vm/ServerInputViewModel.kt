package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData

class ServerInputViewModel : BaseViewModel(){
    val serverAddress = MutableLiveData<String>()
    val serverPort = MutableLiveData<Int>(9330)
}