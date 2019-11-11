package com.bke.datepoll.vm

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.prefs
import com.bke.datepoll.repos.LoginRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SettingsViewModel() : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)


    fun cancelAllRequests() = coroutineContext.cancel()
}
