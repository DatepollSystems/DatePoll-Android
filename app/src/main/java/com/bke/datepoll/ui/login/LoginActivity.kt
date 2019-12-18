package com.bke.datepoll.ui.login

 import android.os.Bundle
 import androidx.appcompat.app.AppCompatActivity
 import androidx.databinding.DataBindingUtil
 import com.bke.datepoll.Prefs
 import com.bke.datepoll.R
 import com.bke.datepoll.databinding.ActivityLoginBinding
 import com.bke.datepoll.vm.LoginViewModel
 import org.koin.android.ext.android.inject
 import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDatabinding()
    }

    private fun initDatabinding(){
        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.vm = loginViewModel
    }
}