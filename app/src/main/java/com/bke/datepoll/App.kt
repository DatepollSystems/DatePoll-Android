package com.bke.datepoll

import android.app.Application
import com.bke.datepoll.network.DatepollServiceFactory
import com.bke.datepoll.database.DatepollDatabase
import com.bke.datepoll.repos.HomeRepository
import com.bke.datepoll.repos.LoginRepository
import com.bke.datepoll.repos.ServerRepository
import com.bke.datepoll.repos.UserRepository
import com.bke.datepoll.vm.LoginViewModel
import com.bke.datepoll.vm.MainViewModel
import com.bke.datepoll.vm.ServerInputViewModel
import com.bke.datepoll.vm.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    // Database
    single { DatepollServiceFactory.createDatepollService(get()) }
    single { DatepollDatabase.getDatabase(androidContext()) }

    // others
    single { Prefs(androidContext()) }
    single { AppObservableHandler() }

    // Repositories
    single { ServerRepository() }
    single { LoginRepository() }
    single { UserRepository() }
    single { HomeRepository() }

    // ViewModels
    viewModel { LoginViewModel() }
    viewModel { MainViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { ServerInputViewModel() }
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule))
        }


    }
}


