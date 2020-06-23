package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.toDBModelList
import com.datepollsystems.datepoll.database.DatepollDatabase
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
}