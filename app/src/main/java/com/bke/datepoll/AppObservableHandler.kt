package com.bke.datepoll

import androidx.lifecycle.MutableLiveData

class AppObservableHandler {
    val showSnackbar = MutableLiveData<String>()
    val progressBar = MutableLiveData<Boolean>()
}