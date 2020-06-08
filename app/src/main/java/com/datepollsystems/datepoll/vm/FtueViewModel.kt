package com.datepollsystems.datepoll.vm

import android.util.Patterns
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Instance
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.repos.LoginRepository
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.experimental.property.inject
import org.koin.ext.getScopeId
import timber.log.Timber
import java.util.*

class FtueViewModel : ViewModel(), KoinComponent{

    val serverInstanceUrl = MutableLiveData<String>()

    val instanceMenu = MutableLiveData<List<Instance>>()
    val instanceMenuState = MutableLiveData<ENetworkState>()
    val instanceClickResult = MutableLiveData<Instance>()

    var successfulNext: Int = R.id.action_ftueSuccessfulFragment_to_ftueServerInstanceFragment
    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginState = MutableLiveData<ENetworkState>()
    val loginSuccessful = MutableLiveData<Boolean>()

    private val datepollRepository: DatepollRepository by inject()
    private val prefs: Prefs by inject()
    private val loginRepository: LoginRepository by inject()


    fun validateServerInstance(url: String): Boolean{
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun loadInstances(){
        viewModelScope.launch(Dispatchers.Default) {
            val l = datepollRepository.loadInstances(instanceMenuState)
            instanceMenu.postValue(l?.instances)
        }
    }

    fun login(){
        viewModelScope.launch(Dispatchers.Default) {
            val response = loginRepository.login(username = userName.value!!, password = password.value!!, loginState = loginState)
            response?.let {
                Timber.i("LoginResponse: $response")
                withContext(Dispatchers.IO) {
                    prefs.isLoggedIn = true
                    prefs.jwt = it.token
                    prefs.jwtRenewalTime = Date().time
                    prefs.session = it.session_token
                    //loginSuccessful.postValue(true)
                }
            }
            /*response?.let {
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
            }*/
        }
    }

    fun setServer(url: String, p: Int){
        prefs.serverAddress = url
        prefs.serverPort = p
    }
}

@JsonClass(generateAdapter = true)
data class QRCodeData(
    val type: String,
    val url: String,
    val session_token: String?
)