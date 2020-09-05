package com.datepollsystems.datepoll.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.db.DatepollDatabase
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.network.InstanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.core.inject
import timber.log.Timber

class CinemaRepository : BaseRepository() {

    private val api: InstanceApi by inject()
    private val db: DatepollDatabase by inject()

    private val cinemaDao = db.cinemaDao()


    val movies = cinemaDao.loadAllMovies()
    val bookedMovies = cinemaDao.selectBookedMovies()
    val moviesWithOrders
        get() = cinemaDao.getAllMoviesWhereCinemaWorker()


    suspend fun loadNotShownMovies(
        force: Boolean = false,
        state: MutableLiveData<ENetworkState>
    ) {
        state.postValue(ENetworkState.LOADING)
        if (force || cinemaDao.getInsertionTime() >= 3600000 || cinemaDao.countAllMovies() == 0L) {
            Timber.i("Loading movies from server")
            val r = apiCall(
                call = { api.getNotShownMovies(prefs.jwt!!) },
                state = state
            )

            r?.let {
                cinemaDao.insertAndDelete(it.movies.toDBModelList())
                Timber.i("Inserted ${it.movies.size} movies")
            }
        } else {
            Timber.i("Loading movies from db")
            state.postValue(ENetworkState.DONE)
        }
    }

    suspend fun applyForMovieWorker(
        movie: MovieDbModel,
        user: UserDbModel,
        state: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        apiCall(
            call = { api.applyForMovieWorker(movie.id, prefs.jwt!!) },
            state = state
        )?.let {
            movie.workerId = user.id.toInt()
            movie.workerName = "${user.firstname} ${user.surname}"
            return movie
        }
        return null
    }

    suspend fun signOutOfMovieWorker(
        movie: MovieDbModel,
        state: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        apiCall(
            call = { api.signOutOfMovieWorker(movie.id, prefs.jwt!!) },
            state = state
        )?.let {
            movie.workerId = null
            movie.workerName = null
            return movie
        }

        return null
    }

    suspend fun applyForEmergencyMovieWorker(
        movie: MovieDbModel,
        user: UserDbModel,
        state: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        apiCall(
            call = { api.applyForEmergencyMovieWorker(movie.id, prefs.jwt!!) },
            state = state
        )?.let {
            movie.emergencyWorkerId = user.id.toInt()
            movie.emergencyWorkerName = "${user.firstname} ${user.surname}"
            withContext(Dispatchers.IO) {
                cinemaDao.updateMovie(movie)
            }

            return movie
        }

        return null
    }

    suspend fun signOutOfEmergencyMovieWorker(
        movie: MovieDbModel,
        state: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        apiCall(
            call = { api.signOutOfEmergencyMovieWorker(movie.id, prefs.jwt!!) },
            state = state
        )?.let {
            movie.emergencyWorkerName = null
            movie.emergencyWorkerId = null
            withContext(Dispatchers.IO) {
                cinemaDao.updateMovie(movie)
            }
            return movie
        }

        return null
    }

    suspend fun bookTickets(
        movie: MovieDbModel,
        ticketAmount: Int,
        state: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        val request = BookTicketsRequestModel(
            ticketAmount = ticketAmount,
            movieId = movie.id.toInt()
        )

        apiCall(
            call = { api.bookTicketsForMovie(prefs.jwt!!, request) },
            state = state
        )?.let {
            movie.bookedTicketsForYourself += it.movieBooking.amount
            movie.bookedTickets += it.movieBooking.amount
            withContext(Dispatchers.IO) {
                cinemaDao.updateMovie(movie)
            }
            return movie
        }
        return null
    }

    suspend fun cancelTicketReservation(
        movieDbModel: MovieDbModel,
        cancelTicketReservationState: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        val currentTickets = movieDbModel.bookedTicketsForYourself
        apiCall(
            call = { api.cancelTicketBooking(movieId = movieDbModel.id, token = prefs.jwt!!) },
            state = cancelTicketReservationState
        )?.let {
            movieDbModel.bookedTicketsForYourself = 0
            movieDbModel.bookedTickets -= currentTickets
            withContext(Dispatchers.IO) {
                cinemaDao.updateMovie(movieDbModel)
            }
            return movieDbModel
        }
        return null
    }

    suspend fun loadOrdersForMovie(movieId: Long): Flow<List<MovieOrder>> {
        return cinemaDao.getAllOrderForMovie(movieId)
    }

    fun getMovieById(movieId: Long): LiveData<MovieDbModel> {
        return cinemaDao.loadMovieByIdFlow(movieId)
    }
}