package com.datepollsystems.datepoll.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.datepollsystems.datepoll.R

class FtueActivity : AppCompatActivity() {

    companion object {
        val qrCodeRes = 1
        val qrCodeData = "qrCodeData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ftue)
    }


}