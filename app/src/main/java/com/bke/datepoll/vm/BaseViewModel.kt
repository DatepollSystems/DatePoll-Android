package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel: ViewModel(), KoinComponent {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    protected val scope = CoroutineScope(coroutineContext)

    fun cancelAllRequests() = coroutineContext.cancel()
}