package com.bke.datepoll.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bke.datepoll.AppObservableHandler
import com.bke.datepoll.R
import com.bke.datepoll.databinding.ActivityMainBinding
import com.bke.datepoll.ui.BaseActivity
import com.bke.datepoll.ui.ServerInputActivity
import com.bke.datepoll.ui.settings.SettingsActivity
import com.bke.datepoll.vm.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalArgumentException

class MainActivity : BaseActivity(),  NavigationView.OnNavigationItemSelectedListener{

    override lateinit var activityView: View
    private val appObservableHandler: AppObservableHandler by inject()

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityView = findViewById(android.R.id.content)
        initDatabinding()
        initUiAndNavigation()
        initObservers()
        actionBar?.setDisplayHomeAsUpEnabled(false)

        mainViewModel.loadUserData()
    }

    private fun initDatabinding(){
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.vm = mainViewModel
    }

    private fun initUiAndNavigation(){
        val toolbar: BottomAppBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_event,
                R.id.nav_slideshow,
                R.id.nav_tools
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupWithNavController(navView, navController)
        navView.setNavigationItemSelectedListener(this)
    }

    private fun initObservers(){

        mainViewModel.user.observe(this, Observer {
            it?.let {
                val s = "${it.firstname} ${it.surname}"
                tvNavHeaderName?.text = s
                tvNavHeaderUsername?.text = it.username
            }
        })

        mainViewModel.logout.observe(this, Observer {
            if(it != null && it){
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

    override fun onStart() {
        super.onStart()
        mainViewModel.loadUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                return true
            }
            R.id.action_logout -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.logout_title)
                    .setMessage(R.string.logout_dialog_desc)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        mainViewModel.logout()
                    }
                    .setNegativeButton(android.R.string.no, null).create().show()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_main)
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentById(R.id.nav_host_main)!!.childFragmentManager.fragments[0].isVisible) {
            moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        drawer_layout.closeDrawers()

        val id = menuItem.itemId

        when(id){
            R.id.nav_event -> findNavController(R.id.nav_host_main).navigate(R.id.action_nav_home_to_eventFragment)
        }

        return true

    }
}
