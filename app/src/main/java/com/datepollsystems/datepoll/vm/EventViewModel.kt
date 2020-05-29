package com.datepollsystems.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.database.model.event.EventDbModel
import com.datepollsystems.datepoll.database.model.event.EventDecisionDbModel
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.repos.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject

class EventViewModel : BaseViewModel() {
    val tag = "EventViewModel"

    private val eventRepository: EventRepository by inject()

    val events = eventRepository.events
    val filteredEvents = eventRepository.filteredEvents
    val decisions = MutableLiveData<List<EventDecisionDbModel>>()
    val decisionClickResult = MutableLiveData<EventDecisionDbModel>()

    var filterChecked = false
    val loadEventsState = MutableLiveData<ENetworkState>()
    val loadDecisionsForEventState = MutableLiveData<ENetworkState>()
    val makeDecisionState = MutableLiveData<ENetworkState>()
    val removeVoteForEventState = MutableLiveData<ENetworkState>()

    fun loadEventData(force: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            eventRepository.loadAllEvents(force = force, state = loadEventsState)
        }
    }



    fun loadDecisionsForEvent(eventId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            decisions.postValue(
                eventRepository.loadDecisionForEvent(
                    eventId,
                    state = loadDecisionsForEventState
                )
            )
        }
    }

    fun voteForEvent(it: EventDecisionDbModel) {
        viewModelScope.launch(Dispatchers.Default) {
            eventRepository.voteForEvent(it, makeDecisionState)
        }
    }

    fun removeEventAnswer(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            eventRepository.removeVoteForEvent(id, removeVoteForEventState)
        }
    }
}