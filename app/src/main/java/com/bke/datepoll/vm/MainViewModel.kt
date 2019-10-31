package com.bke.datepoll.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.data.model.UserModel
import com.bke.datepoll.data.requests.CurrentUserResponseModel
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.repos.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val homeRepository: HomeRepository = HomeRepository(
        DatepollServiceFactory.createDatepollService(),
        DatepollDatabase.getDatabase(application).userDao())

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