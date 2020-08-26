package com.datepollsystems.datepoll.components.main

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.appModule
import com.datepollsystems.datepoll.components.login.FtueActivity
import com.datepollsystems.datepoll.databinding.ActivityMainBinding
import com.datepollsystems.datepoll.networkModule
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules


class MainActivity : AppCompatActivity(), AppBarConfiguration.OnNavigateUpListener {

    private val mainViewModel: MainViewModel by viewModel()

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
        bottomNavigationView?.setupWithNavController(navController)
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

    override fun onBackPressed(){
        if(!findNavController(R.id.nav_host_main).popBackStack())
            moveTaskToBack(true)
    }
}

