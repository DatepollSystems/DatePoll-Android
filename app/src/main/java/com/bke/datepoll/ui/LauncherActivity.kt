package com.bke.datepoll.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.vm.AppViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LauncherActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()

    private val applicationVM: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()


        applicationVM.networkError.observe(this, Observer {
            it.let {
                if (it) {
                    Snackbar.make(this.window.decorView, "Something went wrong!", Snackbar.LENGTH_LONG).show()
                    applicationVM.networkError.postValue(false)
                }
            }
        })


        Thread(Runnable {
            Thread.sleep(3000)

            if(!prefs.JWT.isNullOrEmpty()){
                Log.i("JWT", "JWT is saved")
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
            }
        }).start()
    }
}
