package com.datepollsystems.datepoll.ui.login


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.Prefs

import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentLoginHomeBinding
import com.datepollsystems.datepoll.ui.main.MainActivity
import com.datepollsystems.datepoll.vm.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginHomeFragment : Fragment() {

    private val prefs: Prefs by inject()
    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentLoginHomeBinding>(
            inflater, R.layout.fragment_login_home, container, false
        )
        val view = binding.root

        binding.vm = loginViewModel
        binding.lifecycleOwner = this


        return view
    }

    override fun onStart() {

        val login = activity?.findViewById<Button>(R.id.btnLogin)
        val loading = activity?.findViewById<ProgressBar>(R.id.loading)

        login?.isEnabled = true
        login?.setOnClickListener {
            loading?.visibility = View.VISIBLE
            loginViewModel.login()
        }

        loginViewModel.loginSuccessful.observe(this, Observer {
            it?.let {
                if(it) {
                    loginViewModel.loginSuccessful.value = null
                    prefs.isLoggedIn = true
                    startActivity(Intent(activity, MainActivity::class.java))
                } else {
                    loginViewModel.loginSuccessful.value = null
                    loading?.visibility = View.INVISIBLE
                    activity?.currentFocus?.let { it1 ->
                        Snackbar.make(it1, "Could not login", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        super.onStart()
    }


}
