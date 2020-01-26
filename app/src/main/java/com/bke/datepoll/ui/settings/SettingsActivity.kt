package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bke.datepoll.AppObservableHandler
import com.bke.datepoll.R
import com.bke.datepoll.ui.BaseActivity
import com.bke.datepoll.vm.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : BaseActivity() {

    override lateinit var activityView: View
    private val appObservableHandler: AppObservableHandler by inject()


    private val vm: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        activityView = findViewById(android.R.id.content)


        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        toolbar.title = resources.getString(R.string.settings)
        setSupportActionBar(toolbar)



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.cancelAllRequests()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val check = supportFragmentManager.findFragmentByTag(getString(R.string.settings_home_fragment))?.isVisible
        if(check != null && check){
            finish()
        } else {
            super.onBackPressed()
        }
    }
}

