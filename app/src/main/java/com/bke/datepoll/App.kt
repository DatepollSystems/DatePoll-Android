package com.bke.datepoll

import android.app.Application
import com.bke.datepoll.connection.DatepollServiceFactory
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.repos.HomeRepository
import com.bke.datepoll.repos.LoginRepository
import com.bke.datepoll.vm.LoginViewModel
import com.bke.datepoll.vm.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val prefs: Prefs by lazy {
    App.prefs!!
}

val appModule = module {
    single { DatepollServiceFactory.createDatepollService() }
    single { DatepollDatabase.getDatabase(androidContext()) }

    // Repositories
    single { HomeRepository(get(), get()) }
    single { LoginRepository(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(androidContext(), get()) }
}

class App : Application() {

    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}


