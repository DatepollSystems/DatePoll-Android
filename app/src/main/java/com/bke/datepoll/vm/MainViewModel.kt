package com.bke.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.data.model.UserModel
import com.bke.datepoll.data.requests.CurrentUserResponseModel
import com.bke.datepoll.repos.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val user = MutableLiveData<UserModel>()

    fun getDataOfCurrentUser(){
        scope.launch {
            val response: CurrentUserResponseModel = homeRepository.getCurrentUser()!!
            user.postValue(response.user)
        }
    }

    fun renewToken(){
        scope.launch {
            homeRepository.renewToken()
        }
    }

    fun renewTokenAndRefreshUserData(){
        scope.launch {
            homeRepository.renewToken()
            val response: CurrentUserResponseModel = homeRepository.getCurrentUser()!!
            user.postValue(response.user)
        }
    }
}