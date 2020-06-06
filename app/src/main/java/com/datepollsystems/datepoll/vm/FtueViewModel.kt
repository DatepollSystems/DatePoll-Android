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
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.experimental.property.inject
import org.koin.ext.getScopeId

class FtueViewModel : ViewModel(), KoinComponent{

    val serverInstanceUrl = MutableLiveData<String>()

    val instanceMenu = MutableLiveData<List<Instance>>()
    val instanceMenuState = MutableLiveData<ENetworkState>()
    val instanceClickResult = MutableLiveData<Instance>()

    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginState = MutableLiveData<ENetworkState>()

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
            loginRepository.login(username = userName.value!!, password = password.value!!, loginState = loginState)
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