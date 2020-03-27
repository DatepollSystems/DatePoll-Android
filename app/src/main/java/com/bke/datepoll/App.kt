package com.bke.datepoll

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bke.datepoll.database.DatepollDatabase
import com.bke.datepoll.network.DatepollServiceFactory
import com.bke.datepoll.repos.*
import com.bke.datepoll.ui.settings.themeOptions
import com.bke.datepoll.vm.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.math.sin

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
    single { EventRepository() }

    // ViewModels
    viewModel { LoginViewModel() }
    viewModel { MainViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { SettingsEmailViewModel() }
    viewModel { ServerInputViewModel() }
    viewModel { EventViewModel() }
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule))
        }

        val prefs: Prefs by inject()
        when (prefs.THEME) {
            themeOptions[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            themeOptions[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            themeOptions[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}


