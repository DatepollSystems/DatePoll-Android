package com.datepollsystems.datepoll.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.data.Birthday
import com.datepollsystems.datepoll.data.Booking
import com.datepollsystems.datepoll.data.Event
import com.datepollsystems.datepoll.data.LogoutRequestModel
import com.datepollsystems.datepoll.data.LogoutResponseModel
import com.datepollsystems.datepoll.database.model.PermissionDbModel
import com.datepollsystems.datepoll.database.model.UserDbModel
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.repos.HomeRepository
import com.datepollsystems.datepoll.repos.ServerRepository
import com.datepollsystems.datepoll.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject

class MainViewModel : BaseViewModel() {

    private val tag = "MainViewModel"

    private val prefs: Prefs by inject()
    private val userRepository: UserRepository by inject()
    private val serverRepository: ServerRepository by inject()
    private val homeRepository: HomeRepository by inject()

    val loaded = MutableLiveData<LiveData<UserDbModel>>()
    val user = userRepository.user
    val logout = MutableLiveData<Boolean>(false)
    val permissions = MutableLiveData<List<PermissionDbModel>>()

    private val loadUserState = MutableLiveData<ENetworkState>()
    val loadHomepageState = MutableLiveData<ENetworkState>()

    val birthdays = MutableLiveData<List<Birthday>>()
    val events = MutableLiveData<List<Event>>()
    val bookings = MutableLiveData<List<Booking>>()


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
                Log.i(tag, "start logout process")

                val session = prefs.session!!
                val response: LogoutResponseModel? =
                    serverRepository.logout(
                        LogoutRequestModel(
                            session_token = session
                        )
                    )

                response?.username?.let {
                    Log.i(tag, "logout successful")
                }

                prefs.session = ""
                prefs.jwt = ""
                prefs.isLoggedIn = false
                logout.postValue(true)
            }
        }
    }

    fun loadHomepage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val h = homeRepository.loadHomepage(loadHomepageState)
                Log.i(tag, h.toString())
                birthdays.postValue(h?.birthdays)
                events.postValue(h?.events)
                bookings.postValue(h?.bookings)
            }
        }
    }
}