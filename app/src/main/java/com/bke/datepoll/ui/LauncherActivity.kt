package com.bke.datepoll.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.ui.login.LoginActivity
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()

        Thread(Runnable {
            Thread.sleep(1000)

            if(!prefs.JWT.isNullOrEmpty()){
                Log.i("JWT", "JWT is saved")
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this@LauncherActivity, ServerInputActivity::class.java))
            }
        }).start()
    }
}
