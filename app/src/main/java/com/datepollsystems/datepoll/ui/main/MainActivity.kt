package com.datepollsystems.datepoll.ui.main

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.ActivityMainBinding
import com.datepollsystems.datepoll.ui.BaseActivity
import com.datepollsystems.datepoll.ui.login.ServerInputActivity
import com.datepollsystems.datepoll.ui.settings.SettingsActivity
import com.datepollsystems.datepoll.vm.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), AppBarConfiguration.OnNavigateUpListener {

    override lateinit var activityView: View


    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityView = findViewById(android.R.id.content)

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

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
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
                val i = Intent(this@MainActivity, ServerInputActivity::class.java)
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
        if (supportFragmentManager.findFragmentById(R.id.nav_host_main)!!.childFragmentManager.fragments[0].isVisible) {
            moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }
}

/**
 *
 *  MaterialAlertDialogBuilder(this)
.setTitle(R.string.logout_title)
.setMessage(R.string.logout_dialog_desc)
.setPositiveButton(android.R.string.yes) { _, _ ->
mainViewModel.logout()
}
.setNegativeButton(android.R.string.no, null).create().show()
 */
