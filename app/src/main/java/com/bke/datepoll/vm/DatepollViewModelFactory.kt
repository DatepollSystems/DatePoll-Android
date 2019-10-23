package com.bke.datepoll.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.repos.HomeRepository
import com.bke.datepoll.repos.LoginRepository

class DatepollViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    DatepollServiceFactory.createDatepollService()
                )
            ) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                homeRepository = HomeRepository(
                    DatepollServiceFactory.createDatepollService()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
