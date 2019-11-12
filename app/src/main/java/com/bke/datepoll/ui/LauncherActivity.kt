package com.bke.datepoll.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bke.datepoll.R
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        supportActionBar?.hide()



    }
}
