package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.repos.EventRepository
import kotlinx.coroutines.launch
import org.koin.core.inject

class EventViewModel : BaseViewModel() {
    val tag = "EventViewModel"

    private val eventRepository: EventRepository by inject()

    val events = eventRepository.events

    val loadEventsState = MutableLiveData<ENetworkState>()

    fun loadEventData(){
        scope.launch {
            eventRepository.loadAllEvents(force = false, state = loadEventsState)
        }
    }
}