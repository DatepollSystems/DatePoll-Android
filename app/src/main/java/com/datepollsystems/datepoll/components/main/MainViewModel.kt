package com.datepollsystems.datepoll.components.main

import android.view.View
import androidx.lifecycle.*
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.data.PermissionDbModel
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.repos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class MainViewModel : ViewModel(), KoinComponent {

    private val prefs: Prefs by inject()
    private val userRepository: UserRepository by inject()
    private val serverRepository: ServerRepository by inject()
    private val homeRepository: HomeRepository by inject()
    private val cinemaRepository: CinemaRepository by inject()
    private val appRepository: AppRepository by inject()

    val user = userRepository.user
    val logout = MutableLiveData(false)
    val permissions = MutableLiveData<List<PermissionDbModel>>()

    private val loadUserState = MutableLiveData<ENetworkState>()
    val loadBirthdaysAndBroadcastState = MutableLiveData<ENetworkState>()
    val loadBookedMoviesState = MutableLiveData<ENetworkState>()
    val loadMovieWorkerState = MutableLiveData<ENetworkState>()

    val birthdays = homeRepository.birthdays
    val events = MutableLiveData<List<Event>>()
    val bookings = cinemaRepository.bookedMovies
    val movieWorkerDetails = cinemaRepository.moviesWithOrders.asLiveData()

    val bookingsCardVisible = Transformations.map(bookings) {
        it?.let {
            checkIfAllCardsInvisible()
            if (it.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    val movieWorkerCardVisible = Transformations.map(movieWorkerDetails) {
        it?.let {
            checkIfAllCardsInvisible()
            if (it.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    val birthdayCardVisible = Transformations.map(birthdays) {
        it?.let {
            checkIfAllCardsInvisible()
            if (it.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    val nothingToSeeVisible = MutableLiveData<Int>(View.INVISIBLE)

    private fun checkIfAllCardsInvisible() {
        val first = movieWorkerDetails.value
        val second = bookings.value
        val third = birthdays.value
        first?.let {
            second?.let {
                third?.let {
                    if (
                        first.isEmpty() &&
                        second.isEmpty() &&
                        third.isEmpty()
                    ) {
                        nothingToSeeVisible.value = View.VISIBLE
                        nothingToSeeVisible.postValue(View.VISIBLE)
                    } else {
                        nothingToSeeVisible.value = View.INVISIBLE
                        nothingToSeeVisible.postValue(View.INVISIBLE)
                    }
                }
            }
        }

    }

    fun loadUserData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.getUser(loadUserState, force = true)
            }
        }
    }

    fun logout() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                Timber.i("start logout process")

                val session = prefs.session
                session?.let {
                    val response: LogoutResponseModel? =
                        serverRepository.logout(
                            LogoutRequestModel(
                                session_token = session
                            )
                        )

                    response?.username?.let {
                        Timber.i("logout successful")
                    }
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
        viewModelScope.launch(Dispatchers.Default) {
            homeRepository.loadBirthdaysAndBroadcasts(force, loadBirthdaysAndBroadcastState)
            appRepository.loadApiInfo()
            appRepository.apiInfo.collect {
                if (it.cinemaEnabled) {
                    cinemaRepository.loadNotShownMovies(force, loadBookedMoviesState)
                    homeRepository.fetchMovieOrders(force, loadMovieWorkerState)
                } else {
                    cinemaRepository.deleteAll()
                }
            }
        }
    }
}