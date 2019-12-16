package com.bke.datepoll.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.databinding.ActivityLoginBinding
import com.bke.datepoll.vm.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO create LauncherActivity with initial renew session request if the user was logged in
        //TODO else route to LoginActivity


        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.vm = loginViewModel


        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        login.isEnabled = true
        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.login()
        }

        loginViewModel.loginSuccessful.observe(this, Observer {
            it?.let {
                if(it) {
                    loginViewModel.loginSuccessful.value = null
                    prefs.IS_LOGGED_IN = true
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    loginViewModel.loginSuccessful.value = null
                    loading.visibility = View.INVISIBLE
                    this.currentFocus?.let { it1 ->
                        Snackbar.make(it1, "Could not login", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}