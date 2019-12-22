package com.bke.datepoll.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.animateVisibility
import com.bke.datepoll.appModule
import com.bke.datepoll.databinding.FragmentServerInputBinding
import com.bke.datepoll.vm.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_server_input.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ServerInputFragment : Fragment() {

    private val prefs: Prefs by inject()
    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentServerInputBinding>(
                inflater, R.layout.fragment_server_input, container, false
        )
        val view = binding.root

        binding.vm = loginViewModel
        binding.lifecycleOwner = this


        loginViewModel.serverPort.observeForever{
            Log.i("Observer", it.toString())
        }

        return view
    }

    override fun onStart() {
        btnShowMoreServer.setOnClickListener {
            btnShowMoreServer.isEnabled = false
            advancedServerSettings.animateVisibility(true)
        }

        btnSetServer.setOnClickListener {
            loadingServer.visibility = View.VISIBLE
            if(loginViewModel.serverAddress.value.toString().isNotBlank()) {
                prefs.SERVER_ADDRESS = loginViewModel.serverAddress.value
                val port: Int = loginViewModel.serverPort.value!!
                prefs.SERVER_PORT = port

                stopKoin()
                Log.i("Login", prefs.SERVER_ADDRESS!!)
                Log.i("PORT", prefs.SERVER_PORT.toString())
                startKoin {
                    androidLogger()
                    androidContext(activity!!.applicationContext)
                    modules(appModule)
                }
                it.findNavController().navigate(R.id.action_serverInputFragment_to_loginHomeFragment)
            }
            loadingServer.visibility = View.INVISIBLE
        }
        super.onStart()
    }
}
