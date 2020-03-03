package com.bke.datepoll.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.animateVisibility
import com.bke.datepoll.appModule
import com.bke.datepoll.databinding.ActivityServerInputBinding
import com.bke.datepoll.ui.login.LoginActivity
import com.bke.datepoll.vm.ServerInputViewModel
import kotlinx.android.synthetic.main.activity_server_input.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ServerInputActivity : AppCompatActivity() {

    private val serverInputViewModel: ServerInputViewModel by viewModel()
    private val prefs : Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_input)

        val binding =
                DataBindingUtil.setContentView<ActivityServerInputBinding>(this, R.layout.activity_server_input)
        binding.vm = serverInputViewModel

        btnShowMoreServer.setOnClickListener {
            btnShowMoreServer.isEnabled = false
            advancedServerSettings.animateVisibility(true)
        }

        etServerAddress.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(Patterns.WEB_URL.matcher(s).matches()){
                    btnSetServer.isEnabled = true
                    tiServerDomain.error = ""
                } else {
                    btnSetServer.isEnabled = false
                    tiServerDomain.error = "This is no correct URL"
                }
            }
        })


        btnSetServer.isEnabled = false

        btnSetServer.setOnClickListener {
            loadingServer.visibility = View.VISIBLE
            if(serverInputViewModel.serverAddress.value.toString().isNotBlank()) {
                prefs.SERVER_ADDRESS = serverInputViewModel.serverAddress.value
                val port: Int = serverInputViewModel.serverPort.value!!
                prefs.SERVER_PORT = port

                stopKoin()
                Log.i("Login", prefs.SERVER_ADDRESS!!)
                Log.i("PORT", prefs.SERVER_PORT.toString())
                startKoin {
                    androidLogger()
                    androidContext(applicationContext)
                    modules(appModule)
                }
                startActivity(Intent(this@ServerInputActivity, LoginActivity::class.java))
            }
            loadingServer.visibility = View.INVISIBLE
        }
    }
}
