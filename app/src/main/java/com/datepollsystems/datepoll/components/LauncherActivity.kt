package com.datepollsystems.datepoll.components

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.login.FtueActivity
import com.datepollsystems.datepoll.components.main.MainActivity
import com.datepollsystems.datepoll.core.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LauncherActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()
    private val appViewModel: AppViewModel by viewModel()

    private var activityStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()

        this.lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            if (prefs.isLoggedIn) {
                appViewModel.apiData.observe(this@LauncherActivity, Observer {
                    it?.let {
                        synchronized(activityStarted){
                            if(!activityStarted) {
                                if (it.versionNumber >= 0) {
                                    activityStarted = true
                                    startActivity(
                                        Intent(
                                            this@LauncherActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            }
                        }
                    }
                })
                appViewModel.loadApiInfo()
            } else {
                startActivity(Intent(this@LauncherActivity, FtueActivity::class.java))
            }
        }
    }
}
