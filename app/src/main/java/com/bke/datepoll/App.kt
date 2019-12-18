package com.bke.datepoll

import android.app.Application
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.repos.*
import com.bke.datepoll.vm.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


val appModule = module {
    // Database
    single { DatepollServiceFactory.createDatepollService() }
    single { DatepollDatabase.getDatabase(androidContext()) }

    // others
    single { Prefs(androidContext()) }
    single { AppObservableHandler() }

    // Repositories
    single { ServerRepository(get(), get()) }
    single { HomeRepository(get(), get()) }
    single { LoginRepository(get()) }
    single { UserRepository(get(), get()) }

    // ViewModels
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
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


