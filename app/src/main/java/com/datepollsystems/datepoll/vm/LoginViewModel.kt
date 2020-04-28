package com.datepollsystems.datepoll.vm

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.repos.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject
import java.util.*


class LoginViewModel : BaseViewModel() {

    private val prefs: Prefs by inject()
    private val repository: LoginRepository by inject()

    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginSuccessful = MutableLiveData<Boolean>()

    fun login() {
        viewModelScope.launch(Dispatchers.Default) {
            val user = userName.value!!
            val pass = password.value!!

            if (isUserNameValid(user) && isPasswordValid(pass)) {
                val login = repository.login(user, pass)
                Log.i("LoginResponse", login.toString())
                login?.let {
                    if (it.token.isNotEmpty()) {
                        withContext(Dispatchers.IO) {
                            prefs.jwt = it.token
                            prefs.jwtRenewalTime = Date().time
                            prefs.session = it.session_token
                            loginSuccessful.postValue(true)
                        }
                    } else {
                        loginSuccessful.postValue(false)
                    }
                } ?: run {
                    loginSuccessful.postValue(false)
                }
            }
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
