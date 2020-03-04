package com.bke.datepoll.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.animateVisibility
import com.bke.datepoll.appModule
import com.bke.datepoll.databinding.ActivityServerInputBinding
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.ui.login.LoginActivity
import com.bke.datepoll.vm.ServerInputViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_server_input.*
import kotlinx.android.synthetic.main.activity_server_input.view.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ServerInputActivity : AppCompatActivity() {

    private val serverInputViewModel: ServerInputViewModel by viewModel()
    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_input)

        val binding =
            DataBindingUtil.setContentView<ActivityServerInputBinding>(
                this,
                R.layout.activity_server_input
            )
        binding.vm = serverInputViewModel

        val view = binding.root

        btnShowMoreServer.setOnClickListener {
            view.btnShowMoreServer.isEnabled = false
            view.advancedServerSettings.animateVisibility(true)
        }

        etServerAddress.addTextChangedListener {
            it?.let {
                val s = it.toString()
                if (Patterns.WEB_URL.matcher(s).matches()) {
                    view.btnSetServer.isEnabled = true
                    view.tiServerDomain.error = ""
                } else {
                    view.btnSetServer.isEnabled = false
                    view.tiServerDomain.error = getString(R.string.url_incorrect)
                }
            }
        }


        serverInputViewModel.validateInstanceState.observe(this, Observer {
            it?.let {
                when (it) {
                    ENetworkState.LOADING -> {

                    }
                    ENetworkState.DONE -> {
                        startActivity(Intent(this@ServerInputActivity, LoginActivity::class.java))
                    }

                    ENetworkState.ERROR -> {
                        btnSetServer.isEnabled = true
                        Snackbar.make(
                            binding.root,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()
                        loadingServer.visibility = View.INVISIBLE

                    }
                }

                serverInputViewModel.validateInstanceState.postValue(null)
            }
        })

        view.btnSetServer.isEnabled = false

        view.btnSetServer.setOnClickListener {
            loadingServer.visibility = View.VISIBLE

            //check if there is a datepoll instance running
            btnSetServer.isEnabled = false
            var s = serverInputViewModel.serverAddress.value.toString()
            if (s.contains("https://"))
                s = s.removePrefix("https://")


            val url = "https://${s}"

            prefs.SERVER_ADDRESS = url
            val port: Int = serverInputViewModel.serverPort.value!!
            prefs.SERVER_PORT = port
            stopKoin()
            Log.i("Login", prefs.SERVER_ADDRESS!!)
            Log.i("Port", prefs.SERVER_PORT.toString())
            startKoin {
                androidLogger()
                androidContext(applicationContext)
                modules(appModule)
            }
            serverInputViewModel.validateInstance(s)
        }
    }
}
