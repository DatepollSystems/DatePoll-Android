package com.datepollsystems.datepoll.components.main.cinema

import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.*
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.repos.CinemaRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.MovieOrder
import com.datepollsystems.datepoll.data.MovieOrderTupl
import com.datepollsystems.datepoll.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class CinemaViewModel : ViewModel(), KoinComponent {

    private val repository: CinemaRepository by inject()
    private val userRepository: UserRepository by inject()

    val movies = repository.movies.asLiveData(context = viewModelScope.coroutineContext)
    val detailMovie = MutableLiveData<MovieDbModel>()
    val user = userRepository.user
    val showBottomSheet = MutableLiveData(false)

    val bookCardAmount = MutableLiveData(1)

    val nothingToSeeVisible = Transformations.map(movies) {
        if (it.isEmpty())
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val setWorkerNameVisibility = Transformations.map(detailMovie) {
        if (it.workerId != null)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val unsubscribeMovieWorkerVisibility = Transformations.map(detailMovie) {
        if(it.workerId?.toLong() == user.value?.id)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val unsubscribeMovieEmergencyWorkerVisibility = Transformations.map(detailMovie) {
        if(it.emergencyWorkerId?.toLong() == user.value?.id)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val setAsMovieWorkerButtonVisibility = Transformations.map(detailMovie) {
        if (it.workerId == null)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val setAsMovieEmergencyWorkerButtonVisibility = Transformations.map(detailMovie) {
        if (it.emergencyWorkerId == null)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val setEmergencyWorkerNameVisibility = Transformations.map(detailMovie) {
        if (it.emergencyWorkerId != null)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val availableTickets = Transformations.map(detailMovie) {
        20 - it.bookedTickets
    }

    val cancelReservedTicketsBtnVisibility = Transformations.map(detailMovie) {
        if(it.bookedTicketsForYourself > 0)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val soldOutVisibility = Transformations.map(availableTickets) {
        if(it == 0)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val fabVisible = Transformations.map(availableTickets) {
        if(it == 0)
            View.INVISIBLE
        else
            View.VISIBLE
    }


    val loadMoviesState = MutableLiveData<ENetworkState>()
    val applyForMovieWorkerDetailState = MutableLiveData<ENetworkState>()
    val applyForEmergencyMovieWorkerDetailState = MutableLiveData<ENetworkState>()
    val unsubscribeOfMovieWorkerState = MutableLiveData<ENetworkState>()
    val unsubscribeOfEmergencyMovieWorkerState = MutableLiveData<ENetworkState>()
    val bookTicketState = MutableLiveData<ENetworkState>()
    val cancelTicketReservationState = MutableLiveData<ENetworkState>()

    fun applyForCinemaWorker() {
        viewModelScope.launch(Dispatchers.Default) {
            detailMovie.value?.let { it ->
                repository.applyForMovieWorker(
                    it,
                    userRepository.user.value!!,
                    applyForMovieWorkerDetailState
                )?.let { m ->
                    detailMovie.postValue(m)
                }
            }
        }
    }

    fun applyForEmergencyCinemaWorker() {
        viewModelScope.launch(Dispatchers.Default) {
            detailMovie.value?.let {
                repository.applyForEmergencyMovieWorker(
                    it,
                    userRepository.user.value!!,
                    applyForEmergencyMovieWorkerDetailState
                )?.let { movie ->
                    detailMovie.postValue(movie)
                }
            }
        }
    }

    fun unsubscribeMovieWorker(view: View) {
        viewModelScope.launch(Dispatchers.Default) {
            detailMovie.value?.let {
                repository.signOutOfMovieWorker(
                    it,
                    unsubscribeOfMovieWorkerState
                )?.let { movie ->
                    detailMovie.postValue(movie)
                }
            }
        }
    }

    fun unsubscribeEmergencyMovieWorker(view: View) {
        viewModelScope.launch(Dispatchers.Default) {
            detailMovie.value?.let {
                repository.signOutOfEmergencyMovieWorker(
                    it,
                    unsubscribeOfEmergencyMovieWorkerState
                )?.let { movie ->
                    detailMovie.postValue(movie)
                }
            }
        }
    }
    fun bookTicketOnClick() {
        showBottomSheet.postValue(true)
    }

    fun cancelTickets() {
        viewModelScope.launch {
            repository.cancelTicketReservation(detailMovie.value!!, cancelTicketReservationState)?.let {
                detailMovie.postValue(it)
            }
        }
    }

    fun closeBottomSheet() {
        viewModelScope.launch(Dispatchers.Default) {
            //book tickets
            repository.bookTickets(movie = detailMovie.value!!, ticketAmount = bookCardAmount.value!!, state = bookTicketState)?.let {
                detailMovie.postValue(it)
            }

        }
        showBottomSheet.postValue(false)
        Timber.i(bookCardAmount.value.toString())
    }

    fun loadMovies(force: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.loadNotShownMovies(force, loadMoviesState)
        }
    }
}