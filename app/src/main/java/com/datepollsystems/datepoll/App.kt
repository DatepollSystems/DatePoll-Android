package com.datepollsystems.datepoll

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.datepollsystems.datepoll.components.login.FtueViewModel
import com.datepollsystems.datepoll.components.main.MainViewModel
import com.datepollsystems.datepoll.components.main.calendar.CalendarViewModel
import com.datepollsystems.datepoll.components.main.cinema.CinemaViewModel
import com.datepollsystems.datepoll.network.DatepollServiceFactory
import com.datepollsystems.datepoll.repos.*
import com.datepollsystems.datepoll.components.main.event.EventViewModel
import com.datepollsystems.datepoll.components.settings.SettingsEmailViewModel
import com.datepollsystems.datepoll.components.settings.SettingsViewModel
import com.datepollsystems.datepoll.components.settings.themeOptions
import com.datepollsystems.datepoll.core.DatepollDatabase
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.network.InstanceApi
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import timber.log.Timber.DebugTree


val appModule = module {
    // Database
    single {
        DatepollDatabase.getDatabase(
            androidContext()
        )
    }

    // others
    single { Prefs.getInstance(androidContext()) }

    // Repositories
    single { ServerRepository() }
    single { DatepollRepository() }
    single { LoginRepository() }
    single { UserRepository() }
    single { HomeRepository() }
    single { EventRepository() }
    single { CinemaRepository() }

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { SettingsEmailViewModel() }
    viewModel { EventViewModel() }
    viewModel { CalendarViewModel() }
    viewModel { FtueViewModel() }
    viewModel { CinemaViewModel() }
}

val networkModule = module {
    single { DatepollServiceFactory.createInstanceService(get()) }
    single { DatepollServiceFactory.createDatepollService() }
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, networkModule))
        }

        val prefs: Prefs by inject()
        when (prefs.theme) {
            themeOptions[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            themeOptions[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            themeOptions[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            FakeCrashLibrary.log(
                priority,
                tag,
                message
            )
            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(
                        t
                    )
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(
                        t
                    )
                }
            }
        }
    }
}

class FakeCrashLibrary {
    companion object {
        fun log(priority: Int, tag: String?, message: String?) {
            // TODO add log entry to circular buffer.
        }

        fun logWarning(t: Throwable?) {
            // TODO report non-fatal warning.
        }

        fun logError(t: Throwable?) {
            // TODO report non-fatal error.
        }
    }
}


