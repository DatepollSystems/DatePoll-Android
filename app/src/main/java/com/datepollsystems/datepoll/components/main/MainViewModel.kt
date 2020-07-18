package com.datepollsystems.datepoll.components.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.data.PermissionDbModel
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.repos.HomeRepository
import com.datepollsystems.datepoll.repos.ServerRepository
import com.datepollsystems.datepoll.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class MainViewModel : ViewModel(), KoinComponent {

    private val prefs: Prefs by inject()
    private val userRepository: UserRepository by inject()
    private val serverRepository: ServerRepository by inject()
    private val homeRepository: HomeRepository by inject()

    val user = userRepository.user
    val logout = MutableLiveData(false)
    val permissions = MutableLiveData<List<PermissionDbModel>>()

    private val loadUserState = MutableLiveData<ENetworkState>()
    val loadHomepageState = MutableLiveData<ENetworkState>()

    val birthdays = MutableLiveData<List<Birthday>>()
    val events = MutableLiveData<List<Event>>()
    val bookings = MutableLiveData<List<Booking>>()

    private var time: Date? = null

    fun loadUserData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userRepository.getUser(loadUserState, force = true)
            }
        }
    }

    fun logout() {
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                Timber.i( "start logout process")

                val session = prefs.session!!
                val response: LogoutResponseModel? =
                    serverRepository.logout(
                        LogoutRequestModel(
                            session_token = session
                        )
                    )

                response?.username?.let {
                    Timber.i("logout successful")
                }

                prefs.session = ""
                prefs.jwt = ""
                prefs.isLoggedIn = false
                prefs.serverAddress = null
                prefs.serverPort = 443

                logout.postValue(true)
            }
        }
    }

    fun loadHomepage(force: Boolean = false) {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                //check if last request is too old
                val t = time
                if(force || t == null || (t.time - Date().time) >= 3600000) {
                    val h = homeRepository.loadHomepage(loadHomepageState)
                    Timber.i(h.toString())
                    birthdays.postValue(h?.birthdays)
                    events.postValue(h?.events)
                    bookings.postValue(h?.bookings)
                    time = Date()
                    loadHomepageState.postValue(ENetworkState.DONE)
                }
                else
                    loadHomepageState.postValue(ENetworkState.DONE)
            }
        }
    }
}