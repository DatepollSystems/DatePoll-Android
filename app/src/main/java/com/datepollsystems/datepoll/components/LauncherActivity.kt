package com.datepollsystems.datepoll.components

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.login.FtueActivity
import com.datepollsystems.datepoll.components.main.MainActivity
import org.koin.android.ext.android.inject
import timber.log.Timber

class LauncherActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()

        Thread(Runnable {
            Thread.sleep(1000)

            if(!prefs.jwt.isNullOrEmpty()){
                Timber.i("JWT is saved")
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this@LauncherActivity, FtueActivity::class.java))
            }
        }).start()
    }
}
