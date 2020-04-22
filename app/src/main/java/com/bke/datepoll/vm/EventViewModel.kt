package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bke.datepoll.data.Decision
import com.bke.datepoll.database.model.event.EventDecisionDbModel
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject

class EventViewModel : BaseViewModel() {
    val tag = "EventViewModel"

    private val eventRepository: EventRepository by inject()

    val events = eventRepository.events
    val decisions = MutableLiveData<List<EventDecisionDbModel>>()
    val decisionClickResult = MutableLiveData<EventDecisionDbModel>()

    val loadEventsState = MutableLiveData<ENetworkState>()
    val loadDecisionsForEventState = MutableLiveData<ENetworkState>()
    val makeDecisionState = MutableLiveData<ENetworkState>()
    val removeVoteForEventState = MutableLiveData<ENetworkState>()

    fun loadEventData(force: Boolean = false) {
        scope.launch {
            eventRepository.loadAllEvents(force = force, state = loadEventsState)
        }
    }

    fun loadDecisionsForEvent(eventId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                decisions.postValue(
                    eventRepository.loadDecisionForEvent(
                        eventId,
                        state = loadDecisionsForEventState
                    )
                )
            }
        }
    }

    fun voteForEvent(it: EventDecisionDbModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventRepository.voteForEvent(it, makeDecisionState)
            }
        }
    }

    fun removeEventAnswer(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventRepository.removeVoteForEvent(id, removeVoteForEventState)
            }
        }
    }
}