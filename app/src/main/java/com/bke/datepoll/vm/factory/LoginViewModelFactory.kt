package com.bke.datepoll.vm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.repos.LoginRepository
import com.bke.datepoll.vm.LoginViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    DatepollServiceFactory.createDatepollService()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
