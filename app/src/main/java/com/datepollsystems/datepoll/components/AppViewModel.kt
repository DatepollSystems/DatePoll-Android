package com.datepollsystems.datepoll.components

import androidx.lifecycle.*
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.repos.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class AppViewModel : ViewModel(), KoinComponent {

    private val appRepository: AppRepository by inject()

    //network states
    val apiInfoState = MutableLiveData<ENetworkState>()

    //data holders
    val apiData = appRepository.apiInfo.asLiveData()


    fun loadApiInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.loadApiInfo()
        }
    }
}