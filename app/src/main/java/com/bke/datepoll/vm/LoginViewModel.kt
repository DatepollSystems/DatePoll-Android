package com.bke.datepoll.vm

import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.repos.LoginRepository
import kotlinx.coroutines.launch
import java.util.*


class LoginViewModel(
    private val prefs: Prefs,
    private val repository: LoginRepository
) : BaseViewModel() {

    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginSuccessful = MutableLiveData<Boolean>()
    val serverAddress = MutableLiveData<String>()
    val serverPort = MutableLiveData<Int>(9330)
    val serverPortDisplay = MutableLiveData<String>(serverPort.value.toString())

    fun checkIfServiceIsOnline(){
        scope.launch {
            val online = repository.isServiceOnline()!!.string()
            Log.i("response", online)
            userName.postValue(online)
        }
    }

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
