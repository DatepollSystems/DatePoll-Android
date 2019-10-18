package com.bke.datepoll.vm

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.connection.RetroFactory
import com.bke.datepoll.repos.LoginRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {


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


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : LoginRepository = LoginRepository(RetroFactory.createDatepollService())


    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

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
            Log.i("username", user)
            Log.i("password", pass)

            if(isUserNameValid(user) && isPasswordValid(password.value!!)){
                val login = repository.login(userName.value!!, password.value!!)
                //TODO work with the response.
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}
