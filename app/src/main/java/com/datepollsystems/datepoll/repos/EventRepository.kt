package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.db.DatepollDatabase
import com.datepollsystems.datepoll.db.dao.EventDao
import com.datepollsystems.datepoll.components.main.event.model.*
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.network.InstanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import timber.log.Timber
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
            Timber.i("Load events from server")
            val r: GetAllEventsResponseMsg? = apiCall(
                call = { api.getAllEvents(prefs.jwt!!) },
                state = state
            )

            r?.let { response ->
                withContext(Dispatchers.IO) {
                    Timber.i("Try to fetch new data into db")

                    val idList = response.events.map { e -> e.id }
                    eventDao.deleteAllWhereNotInList(idList)

                    for (e in response.events) {
                        val data = e.getDbData()

                        eventDao.addEvent(data.event)

                        data.decisions?.let {
                            eventDao.addDecisions(it)
                        }

                        data.dates?.let {
                            eventDao.addDates(it)
                        }
                        Timber.i("Fetched events in DB")
                    }
                }
            }
        }
    }

    suspend fun loadDecisionForEvent(
        eventId: Int
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