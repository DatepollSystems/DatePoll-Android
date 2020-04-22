package com.bke.datepoll.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.data.Birthday
import com.bke.datepoll.data.Booking
import com.bke.datepoll.data.Event
import com.bke.datepoll.data.LogoutRequestModel
import com.bke.datepoll.data.LogoutResponseModel
import com.bke.datepoll.database.model.PermissionDbModel
import com.bke.datepoll.database.model.UserDbModel
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.HomeRepository
import com.bke.datepoll.repos.ServerRepository
import com.bke.datepoll.repos.UserRepository
import kotlinx.coroutines.launch
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
        scope.launch {
            userRepository.getUser(loadUserState, force = true)
        }
    }

    fun logout() {
        scope.launch {
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

    fun loadHomepage() {
        scope.launch {
            val h = homeRepository.loadHomepage(loadHomepageState)
            Log.i(tag, h.toString())
            birthdays.postValue(h?.birthdays)
            events.postValue(h?.events)
            bookings.postValue(h?.bookings)
        }
    }
}