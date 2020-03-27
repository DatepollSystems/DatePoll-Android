package com.bke.datepoll.repos

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
        events.value?.let {
            if (force || it.isEmpty() || (it[0].event.insertedAt + 3600000) < Date().time) {
                //load data from server
                val r: GetAllEventsResponseMsg? = apiCall(
                    call = { api.getAllEvents(prefs.JWT!!) },
                    state = state
                )

                r?.let {
                    //Fetch data in db


                    return events
                }

                return events
            } else {
                //load data form db
                return events
            }
        }

        return null
    }


}