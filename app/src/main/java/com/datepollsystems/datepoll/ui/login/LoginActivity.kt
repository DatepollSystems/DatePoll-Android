package com.datepollsystems.datepoll.ui.login

 import android.os.Bundle
 import androidx.appcompat.app.AppCompatActivity
 import androidx.databinding.DataBindingUtil
 import com.datepollsystems.datepoll.Prefs
 import com.datepollsystems.datepoll.R
 import com.datepollsystems.datepoll.databinding.ActivityLoginBinding
 import com.datepollsystems.datepoll.vm.LoginViewModel
 import org.koin.android.ext.android.inject
 import org.koin.androidx.viewmodel.ext.android.viewModel

open class LoginActivity : AppCompatActivity() {

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