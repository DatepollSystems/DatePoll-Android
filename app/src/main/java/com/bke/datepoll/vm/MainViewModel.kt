package com.bke.datepoll.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bke.datepoll.data.requests.CurrentUserResponseModel
import com.bke.datepoll.db.model.PermissionDbModel
import com.bke.datepoll.db.model.UserDbModel
import com.bke.datepoll.repos.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MainViewModel(context: Context, private val homeRepository: HomeRepository) : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val loaded = MutableLiveData<CurrentUserResponseModel>()
    val user = MediatorLiveData<UserDbModel>()
    val permissions = MutableLiveData<List<PermissionDbModel>>()

    fun storeUser(user: CurrentUserResponseModel): LiveData<UserDbModel>{
        return homeRepository.storeUser(user).user
    }

    fun loadUserData() {
       scope.launch {
            Log.i("try to", "do")
            val response: CurrentUserResponseModel? = homeRepository.getCurrentUser()
            let {
               loaded.postValue(response)
            }
        }
    }

    fun renewDataOfCurrentUser(){
        //TODO Check if data is older than 1 hour!!! remove test code

        scope.launch {
            Log.i("try to", "do")
            val user = user.value!!
            val new = UserDbModel(user.id, user.title, user.firstname, user.surname, "geht", user.birthday, user.join_date, user.streetname, user.zipcode, user.location,user.activated, user.activity, user.force_password_change)
            homeRepository.updateUser(new)
        }
    }

    fun renewToken(){
        scope.launch {
            Log.i("try to", "do")
            homeRepository.renewToken()
        }
    }
}