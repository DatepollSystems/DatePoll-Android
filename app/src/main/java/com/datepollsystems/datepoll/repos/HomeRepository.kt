package com.datepollsystems.datepoll.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.BirthdayDbModel
import com.datepollsystems.datepoll.data.HomeScreen
import com.datepollsystems.datepoll.db.dao.DatepollDatabase
import com.datepollsystems.datepoll.network.InstanceApi
import kotlinx.coroutines.Dispatchers
import org.koin.core.inject
import java.util.*

class HomeRepository : BaseRepository() {

    private val api: InstanceApi by inject()
    private val db: DatepollDatabase by inject()

    private val birthdayDao = db.birthdayDao()
    private val cinemaDao = db.cinemaDao()

    val birthdays = birthdayDao.getAllLiveData()

    suspend fun loadBirthdaysAndBroadcasts(
        force: Boolean = false,
        state: MutableLiveData<ENetworkState>
    ) {
        with(Dispatchers.IO) {
            if (force || birthdayDao.getCount() == 0 || birthdayDao.getInsertionTimeOfElements() - Date().time >= 3600000) {
                //fetch new data from server

                val homeScreen = with(Dispatchers.Default) {
                    apiCall(
                        call = { api.getHomepage(prefs.jwt!!) },
                        state = state
                    )
                }

                homeScreen?.let { homeSc ->
                    val nameList = homeSc.birthdays.map {
                        it.name.toString()
                    }
                    val dateList = homeSc.birthdays.map {
                        it.date.toString()
                    }

                    birthdayDao.deleteAllWhereNotInList(nameList = nameList, dateList = dateList)

                    for (b in homeSc.birthdays) {
                        birthdayDao.insertWithChecking(b.toDbModel())
                    }
                }
            }
        }
    }

    suspend fun fetchMovieOrders(
        force: Boolean = false,
        state: MutableLiveData<ENetworkState>
    ) {
        with(Dispatchers.IO) {
            if (force || cinemaDao.countOrders() == 0L || cinemaDao.getInsertionTime() - Date().time >= 3600000) {
                val response = with(Dispatchers.Default) {
                    apiCall(
                        call = { api.getMovieWorkerDetails(prefs.jwt!!) },
                        state = state
                    )
                }

                response?.let { resp ->
                    cinemaDao.deleteAllOrders()
                    resp.movies.forEach {
                        val orders = it.toDbModelData()
                        cinemaDao.insertAllOrders(orders)
                    }
                }
            }
        }
    }
}
