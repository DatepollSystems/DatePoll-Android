package com.datepollsystems.datepoll.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.database.DatepollDatabase
import com.datepollsystems.datepoll.database.dao.EventDao
import com.datepollsystems.datepoll.database.model.event.*
import com.datepollsystems.datepoll.network.InstanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import java.util.*

class EventRepository : BaseRepository() {
    private val tag = "EventRepository"

    private val api: InstanceApi by inject()
    private val db: DatepollDatabase by inject()

    private val eventDao: EventDao = db.eventDao()

    val events = eventDao.loadAllEvents()
    val filteredEvents = eventDao.getFilteredEvents()

    suspend fun loadAllEvents(force: Boolean, state: MutableLiveData<ENetworkState>) {
        val list = events.value
        if (force || list == null || list.isEmpty() || (list[0].insertedAt + 3600000) < Date().time) {
            Log.i(tag, "Load events from server")
            val r: GetAllEventsResponseMsg? = apiCall(
                call = { api.getAllEvents(prefs.jwt!!) },
                state = state
            )

            r?.let { response ->
                withContext(Dispatchers.IO) {
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
        }
    }

    suspend fun loadDecisionForEvent(
        eventId: Int,
        state: MutableLiveData<ENetworkState>
    ): List<EventDecisionDbModel> {
        return withContext(Dispatchers.IO) {
            eventDao.loadDecisionsForEvent(eventId)
        }
    }

    suspend fun voteForEvent(d: EventDecisionDbModel, state: MutableLiveData<ENetworkState>) {

        val requestData = VoteForEventRequestDto(
            decisionId = d.id,
            additionalInfo = "",
            eventId = d.eventId
        )
        apiCall(
            call = { api.voteForEvent(prefs.jwt!!, requestData) },
            state = state
        )?.let {
            withContext(Dispatchers.IO) {
                eventDao.addUserDecision(it)
            }
            return
        }

        state.postValue(ENetworkState.ERROR)
    }

    suspend fun removeVoteForEvent(id: Int, state: MutableLiveData<ENetworkState>) {
        val r = apiCall(
            call = { api.removeVoteForEvent(id, prefs.jwt!!) },
            state = state
        )

        r?.let {
            withContext(Dispatchers.IO) {
                eventDao.removeUserDecision(id)
            }
        }
    }
}