package com.datepollsystems.datepoll.components.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Instance
import com.datepollsystems.datepoll.repos.DatepollRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.repos.LoginRepository
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class FtueViewModel : ViewModel(), KoinComponent {

    val serverInstanceUrl = MutableLiveData<String>()

    val instanceMenu = MutableLiveData<List<Instance>>()
    val instanceMenuState = MutableLiveData<ENetworkState>()
    val instanceClickResult = MutableLiveData<Instance>()

    var successfulNext: Int = R.id.action_ftueSuccessfulFragment_to_ftueServerInstanceFragment
    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginState = MutableLiveData<ENetworkState>()

    val firstChangePassword = MutableLiveData<String>()
    val confirmFirstChangePassword = MutableLiveData<String>()

    val firstPasswdState = MutableLiveData<ENetworkState>()


    private val datepollRepository: DatepollRepository by inject()
    private val prefs: Prefs by inject()
    private val loginRepository: LoginRepository by inject()


    fun validateServerInstance(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun loadInstances() {
        viewModelScope.launch(Dispatchers.Default) {
            val l = datepollRepository.loadInstances(instanceMenuState)
            instanceMenu.postValue(l?.instances)
        }
    }

    fun login() {
        viewModelScope.launch(Dispatchers.Default) {
            val response = loginRepository.login(
                username = userName.value!!,
                password = password.value!!,
                loginState = loginState
            )
            response?.let {
                withContext(Dispatchers.IO) {
                    prefs.isLoggedIn = true
                    prefs.jwt = it.token
                    prefs.jwtRenewalTime = Date().time
                    prefs.session = it.session_token

                    //Maybe login state too, otherwise it could be that the prefs haven't saved

                }
            }
        }
    }

    fun setFirstPasswd() {
        viewModelScope.launch(Dispatchers.Default) {
            val response = loginRepository.setFirstPasswd(
                username = userName.value!!,
                newPasswd = firstChangePassword.value!!,
                oldPasswd = password.value!!,
                state = firstPasswdState
            )

            response?.let {
                withContext(Dispatchers.IO) {
                    prefs.isLoggedIn = true
                    prefs.jwt = it.token
                    prefs.jwtRenewalTime = Date().time
                    prefs.session = it.session_token
                }
            }
        }
    }

    fun setServer(url: String, p: Int) {
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