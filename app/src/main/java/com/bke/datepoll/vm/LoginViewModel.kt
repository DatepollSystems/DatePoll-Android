package com.bke.datepoll.vm

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.repos.LoginRepository
import kotlinx.coroutines.launch
import org.koin.core.inject
import java.util.*


class LoginViewModel: BaseViewModel() {

    private val prefs: Prefs by inject()
    private val repository: LoginRepository by inject()

    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginSuccessful = MutableLiveData<Boolean>()

    fun login(){
        scope.launch {
            val user = userName.value!!
            val pass = password.value!!

            if(isUserNameValid(user) && isPasswordValid(pass)){
                val login = repository.login(user, pass)
                Log.i("LoginResponse", login.toString())
                login?.let {
                    if (it.token.isNotEmpty()) {
                        prefs.JWT = it.token
                        prefs.JWT_RENEWAL_TIME = Date().time
                        prefs.SESSION = it.session_token
                        loginSuccessful.postValue(true)
                    } else {
                        loginSuccessful.postValue(false)
                    }
                } ?:run {
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
