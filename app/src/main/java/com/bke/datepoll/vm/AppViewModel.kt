package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {

    val networkError = MutableLiveData<Boolean>(false)

    init {

    }
}