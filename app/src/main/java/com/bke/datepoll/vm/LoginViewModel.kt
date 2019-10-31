package com.bke.datepoll.vm

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.prefs
import com.bke.datepoll.repos.LoginRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoginViewModel(application: Application) : AndroidViewModel(application) {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : LoginRepository = LoginRepository(DatepollServiceFactory.createDatepollService())


    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginSuccessful = MutableLiveData<Boolean>()

    fun checkIfServiceIsOnline(){
        scope.launch {
            val online = repository.isServiceOnline()!!.string()
            Log.i("repsonse", online)
            userName.postValue(online)
        }
    }

    fun login(){
        scope.launch {
            val user = userName.value!!
            val pass = password.value!!

            if(isUserNameValid(user) && isPasswordValid(pass)){
                val login = repository.login(user, pass)
                Log.i("LoginRepsonse", login.toString())
                login?.let {
                    if (it.token.isNotEmpty()) {
                        prefs.JWT = it.token
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

    fun cancelAllRequests() = coroutineContext.cancel()
}
