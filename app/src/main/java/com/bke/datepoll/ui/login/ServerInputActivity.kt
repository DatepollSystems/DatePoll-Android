package com.bke.datepoll.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.animateVisibility
import com.bke.datepoll.appModule
import com.bke.datepoll.databinding.ActivityServerInputBinding
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.vm.ServerInputViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_server_input.*
import kotlinx.android.synthetic.main.activity_server_input.view.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ServerInputActivity : AppCompatActivity() {

    private val tag = "ServerInputActivity"

    private val serverInputViewModel: ServerInputViewModel by viewModel()
    private val prefs: Prefs by inject()

    private val qrCodeRes = 1
    private val qrCodeData = "qrCodeData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_input)

        val binding =
            DataBindingUtil.setContentView<ActivityServerInputBinding>(
                this,
                R.layout.activity_server_input
            )
        binding.lifecycleOwner = this
        binding.vm = serverInputViewModel


        val view = binding.root

        view.btnShowMoreServer.setOnClickListener {
            view.btnShowMoreServer.isEnabled = false
            view.advancedServerSettings.animateVisibility(true)
        }

        view.etServerAddress.addTextChangedListener {
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

        view.btnQRCodeScan.setOnClickListener {
            startActivityForResult(Intent(this, QrCodeScanActivity::class.java), qrCodeRes)
        }

        view.btnSetServer.isEnabled = false

        view.btnSetServer.setOnClickListener {
            loadingServer.visibility = View.VISIBLE

            //check if there is a datepoll instance running
            btnSetServer.isEnabled = false
            var s = serverInputViewModel.serverAddress.value.toString()
            if (s.contains("https://"))
                s = s.removePrefix("https://")


            val url = "https://${s}"

            prefs.serverAddress = url
            val port: Int = serverInputViewModel.serverPort.value!!
            prefs.serverPort = port
            stopKoin()
            Log.i("Login", prefs.serverAddress!!)
            Log.i("Port", prefs.serverPort.toString())
            startKoin {
                androidLogger()
                androidContext(applicationContext)
                modules(appModule)
            }
            serverInputViewModel.validateInstance(s)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == qrCodeRes) {
            if (resultCode == Activity.RESULT_OK) {
                val d = data?.getStringExtra(qrCodeData)
                Log.i(tag, "QrScan successfully")
                val dataObj = mapResultIntoObj(d)
                dataObj?.let {
                    val url = dataObj.url.removePrefix("https://")
                    serverInputViewModel.serverAddress.value = url
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun mapResultIntoObj(s: String?): QRCodeData? {
        Log.i(tag, "Try to map it into object")
        s?.let {
            val m = Moshi.Builder().build()
            val adapter = m.adapter(QRCodeData::class.java)
            return adapter.fromJson(s)
        }
        Log.e(tag, "Result is not a datepoll qr code")
        return null
    }
}

@JsonClass(generateAdapter = true)
data class QRCodeData(
    val type: String,
    val url: String,
    val session_token: String?
)