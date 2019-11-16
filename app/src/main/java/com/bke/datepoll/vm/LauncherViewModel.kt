package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.Prefs

class LauncherViewModel(
    private val prefs: Prefs
    ) : ViewModel(){

    val koinLoaded = MutableLiveData<Boolean>(true)
}