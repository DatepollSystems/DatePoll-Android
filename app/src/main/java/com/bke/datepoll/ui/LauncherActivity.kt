package com.bke.datepoll.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.appModule
import kotlinx.android.synthetic.main.activity_launcher.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LauncherActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_launcher)

        supportActionBar?.hide()





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
