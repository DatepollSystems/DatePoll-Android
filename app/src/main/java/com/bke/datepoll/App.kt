package com.bke.datepoll

import android.app.Application
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.repos.HomeRepository
import com.bke.datepoll.repos.LoginRepository
import com.bke.datepoll.vm.AppViewModel
import com.bke.datepoll.vm.LoginViewModel
import com.bke.datepoll.vm.MainViewModel
import com.bke.datepoll.vm.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


val appModule = module {
    // Database
    single { DatepollServiceFactory.createDatepollService() }
    single { DatepollDatabase.getDatabase(androidContext()) }

    // Preferences
    single { Prefs(androidContext()) }

    // Repositories
    single { HomeRepository(get(), get()) }
    single { LoginRepository(get()) }

    // ViewModels
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { AppViewModel() }
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }


    }
}


