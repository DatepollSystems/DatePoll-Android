package com.datepollsystems.datepoll.components.main

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.appModule
import com.datepollsystems.datepoll.components.AppViewModel
import com.datepollsystems.datepoll.components.login.FtueActivity
import com.datepollsystems.datepoll.data.ApiModel
import com.datepollsystems.datepoll.databinding.ActivityMainBinding
import com.datepollsystems.datepoll.networkModule
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity(), AppBarConfiguration.OnNavigateUpListener {

    private val mainViewModel: MainViewModel by viewModel()
    private val appViewModel: AppViewModel by viewModel()

    private var bottomNavigationDrawn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(networkModule)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.vm = mainViewModel

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_main)
        setupNavigation(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }
        }

        initObservers()

        mainViewModel.loadUserData()
    }

    private fun setupNavigation(navController: NavController) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        lifecycleScope.launch {
            val menu: Menu = bottomNavigationView.menu
            val apiModel = appViewModel.apiData.value
            if(apiModel != null && Date().time - apiModel.insertedAt < 60000)
                setupMenu(menu, apiModel)
            else {
                appViewModel.apiData.observe(this@MainActivity, Observer {
                    it?.let { setupMenu(menu, it) }
                })
                appViewModel.loadApiInfo()
            }

            bottomNavigationView?.setupWithNavController(navController)
        }
    }

    private fun setupMenu(menu: Menu, api: ApiModel) {
        synchronized(bottomNavigationDrawn) {
            if (!bottomNavigationDrawn && Date().time - api.insertedAt < 60000) {
                Timber.i("Setup bottom navigation")
                menu.add(Menu.NONE, R.id.nav_home, Menu.NONE, getString(R.string.home))
                    .setIcon(R.drawable.ic_home_black)

                if (api.eventsEnabled)
                    menu.add(Menu.NONE, R.id.nav_events, Menu.NONE, getString(R.string.events))
                        .setIcon(R.drawable.ic_assessment)

                menu.add(
                    Menu.NONE,
                    R.id.nav_calendar,
                    Menu.NONE,
                    getString(R.string.menu_calendar)
                )
                    .setIcon(R.drawable.ic_event)

                if (api.cinemaEnabled)
                    menu.add(Menu.NONE, R.id.nav_cinema, Menu.NONE, getString(R.string.cinema))
                        .setIcon(R.drawable.ic_movie_black_24dp)

                menu.add(Menu.NONE, R.id.nav_settings, Menu.NONE, getString(R.string.settings))
                    .setIcon(R.drawable.ic_settings_black_24dp)

                bottomNavigationDrawn = true
            }
        }
    }


    private fun initObservers() {

        mainViewModel.user.observe(this, Observer {
            it?.let {
                val s = "${it.firstname} ${it.surname}"
                tvNavHeaderName?.text = s
                tvNavHeaderUsername?.text = it.username
            }
        })

        mainViewModel.logout.observe(this, Observer {
            if (it != null && it) {
                unloadKoinModules(networkModule)
                loadKoinModules(networkModule)
                val i = Intent(this@MainActivity, FtueActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                mainViewModel.logout.value = false
                finish()
            }
        })
    }

    override fun onNavigateUp(): Boolean {
        return NavigationUI.navigateUp(findNavController(R.id.nav_host_main), drawer_layout)
    }

    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host_main).popBackStack())
            moveTaskToBack(true)
    }
}

