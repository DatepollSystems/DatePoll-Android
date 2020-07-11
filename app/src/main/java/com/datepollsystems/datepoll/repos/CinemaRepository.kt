package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.data.toDBModelList
import com.datepollsystems.datepoll.database.DatepollDatabase
import com.datepollsystems.datepoll.database.model.UserDbModel
import com.datepollsystems.datepoll.network.InstanceApi
import org.koin.core.inject
import timber.log.Timber

class CinemaRepository : BaseRepository() {

    private val api: InstanceApi by inject()
    private val db: DatepollDatabase by inject()

    private val cinemaDao = db.cinemaDao()


    val movies = cinemaDao.loadAllMovies()

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
            movie.workerId =  user.id.toInt()
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
            call = { api.signOutOfMovieWorker(movie.id, prefs.jwt!!)},
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
            movie.emergencyWorkerId =  user.id.toInt()
            movie.emergencyWorkerName = "${user.firstname} ${user.surname}"
            return movie
        }

        return null
    }

    suspend fun signOutOfEmergencyMovieWorker(
        movie: MovieDbModel,
        state: MutableLiveData<ENetworkState>
    ): MovieDbModel? {
        apiCall(
            call = { api.signOutOfEmergencyMovieWorker(movie.id, prefs.jwt!!)},
            state = state
        )?.let {
            movie.emergencyWorkerName = null
            movie.emergencyWorkerId = null
            return movie
        }

        return null
    }
}