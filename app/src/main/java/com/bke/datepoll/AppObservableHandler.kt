package com.bke.datepoll

import androidx.lifecycle.MutableLiveData

class AppObservableHandler {

    private val tag = "AppViewModel"

    val showSnackbar = MutableLiveData<String>()
    val progressBar = MutableLiveData<Boolean>()
}