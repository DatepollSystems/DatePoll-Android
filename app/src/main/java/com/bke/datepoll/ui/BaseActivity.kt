package com.bke.datepoll.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bke.datepoll.AppObservableHandler
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

@SuppressLint("Registered")
abstract class BaseActivity: AppCompatActivity() {

    abstract val activityView: View
    private val handler: AppObservableHandler by inject()

    init {
        handler.showSnackbar.observe(this, Observer {
           if(it != null) {
                Snackbar.make(activityView, it, Snackbar.LENGTH_LONG).show()
                handler.showSnackbar.postValue(null)
            }
        })
    }
}