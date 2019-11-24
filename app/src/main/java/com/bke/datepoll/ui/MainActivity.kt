package com.bke.datepoll.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bke.datepoll.R
import com.bke.datepoll.databinding.ActivityMainBinding
import com.bke.datepoll.ui.settings.SettingsActivity
import com.bke.datepoll.vm.AppViewModel
import com.bke.datepoll.vm.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val mainViewModel: MainViewModel by viewModel()
    private val appViewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            appViewModel.networkError.value = true
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initObservers(){
        appViewModel.networkError.observe(this, Observer {
            it.let {
                if(it) {
                    Snackbar.make(mainLayout, "Ups... something went wrong", Snackbar.LENGTH_LONG).show()
                    appViewModel.networkError.value = false
                }
            }
        })

        mainViewModel.user.observe(this, Observer { u ->
            val s = "${u.firstname} ${u.surname}"
            tvName.text = s
            tvUserName.text = u.username
        })

        mainViewModel.loaded.observe(this, Observer {
            if(it != null){
                mainViewModel.user.addSource(mainViewModel.storeUser(it)){ user ->
                    mainViewModel.user.value = user
                }

                findNavController(R.id.nav_host_main).navigate(R.id.action_home_loaded)

                mainViewModel.loaded.value = null
            }
        })

        mainViewModel.logout.observe(this, Observer {
            if(it != null && it){
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                mainViewModel.logout.value = false
            }
        })
    }



    override fun onRestart() {
        super.onRestart()
        mainViewModel.renewDataOfCurrentUser()
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
                mainViewModel.logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentById(R.id.nav_host_main)!!.childFragmentManager.fragments[0].isVisible) {
            moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }
}
