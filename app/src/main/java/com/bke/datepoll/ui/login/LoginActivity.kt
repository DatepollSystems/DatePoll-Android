package com.bke.datepoll.ui.login

 import android.os.Bundle
 import androidx.appcompat.app.AppCompatActivity
 import androidx.databinding.DataBindingUtil
 import androidx.lifecycle.Observer
 import com.bke.datepoll.Prefs
 import com.bke.datepoll.R
 import com.bke.datepoll.databinding.ActivityLoginBinding
 import com.bke.datepoll.vm.LoginViewModel
 import org.koin.android.ext.android.inject
 import org.koin.androidx.viewmodel.ext.android.viewModel

open class LoginActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDatabinding()
        loginViewModel.serverPortDisplay.observe(this, Observer {
            if (it != null && it.isNotBlank()) {
                loginViewModel.serverPort.value = it.toInt()
            }
        })

        loginViewModel.serverPort.observe(this, Observer {
            if(it != null && it.toString() != loginViewModel.serverPortDisplay.value)
                loginViewModel.serverPortDisplay.value = it.toString()
        })

    }

    private fun initDatabinding(){
        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.vm = loginViewModel
    }
}