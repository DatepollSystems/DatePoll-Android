package com.bke.datepoll.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.database.DatepollDatabase
import com.bke.datepoll.database.dao.EventDao
import com.bke.datepoll.database.model.event.EventWithDecisions
import com.bke.datepoll.database.model.event.GetAllEventsResponseMsg
import com.bke.datepoll.network.DatepollApi
import org.koin.core.inject
import java.util.*

class EventRepository : BaseRepository("EventRepository") {
    private val tag = "EventRepository"

    private val api: DatepollApi by inject()
    private val db: DatepollDatabase by inject()

    private val eventDao: EventDao = db.eventDao()

    suspend fun loadAllEvents(force: Boolean, state: MutableLiveData<ENetworkState>)
            : LiveData<List<EventWithDecisions>>? {

        val events = eventDao.loadAllEvents()
        events.value?.let { list ->
            if (force || list.isEmpty() || (list[0].event.insertedAt + 3600000) < Date().time) {
                Log.i(tag, "Load events from server")
                val r: GetAllEventsResponseMsg? = apiCall(
                    call = { api.getAllEvents(prefs.JWT!!) },
                    state = state
                )

                r?.let { response ->
                    Log.i(tag, "Try to fetch new data into db")
                    for (e in response.events) {
                        val data = e.getDbData()

                        eventDao.addEvent(data.event)

                        data.decisions?.let {
                            eventDao.addDecisions(it)
                        }

                        data.dates?.let {
                            eventDao.addDates(it)
                        }
                        Log.i(tag, "Fetched events in DB")
                    }
                }
            }

            return events
        }
        return null
    }


}