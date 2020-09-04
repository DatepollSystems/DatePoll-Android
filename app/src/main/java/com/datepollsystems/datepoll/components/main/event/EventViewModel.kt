package com.datepollsystems.datepoll.components.main.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDbModel
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.repos.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class EventViewModel : ViewModel(), KoinComponent {
    val tag = "EventViewModel"

    private val eventRepository: EventRepository by inject()

    val events = eventRepository.events
    val filteredEvents = eventRepository.filteredEvents
    val decisions = MutableLiveData<List<EventDecisionDbModel>>()
    val decisionClickResult = MutableLiveData<EventDecisionDbModel>()
    val changeList = MutableLiveData<Boolean>()

    var filterChecked = MutableLiveData<Boolean>(false)
    val loadEventsState = MutableLiveData<ENetworkState>()
    val makeDecisionState = MutableLiveData<ENetworkState>()
    val removeVoteForEventState = MutableLiveData<ENetworkState>()

    fun switchChanged(checked: Boolean){
        changeList.postValue(checked)
    }

    fun loadEventData(force: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            eventRepository.loadAllEvents(force = force, state = loadEventsState)
        }
    }

    fun loadDecisionsForEvent(eventId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            decisions.postValue(
                eventRepository.loadDecisionForEvent(
                    eventId
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