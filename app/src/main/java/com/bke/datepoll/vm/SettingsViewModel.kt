package com.bke.datepoll.vm

import androidx.lifecycle.ViewModel
import com.bke.datepoll.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext


class SettingsViewModel(private val prefs: Prefs) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)


    fun cancelAllRequests() = coroutineContext.cancel()
}
