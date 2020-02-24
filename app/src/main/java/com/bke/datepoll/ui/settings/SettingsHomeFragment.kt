package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentSettingsHomeBinding
import com.bke.datepoll.ui.main.VoteBottomSheetDialog
import com.bke.datepoll.vm.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings_home.*
import kotlinx.android.synthetic.main.fragment_settings_home.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsHomeFragment : Fragment() {

    private val vm: SettingsViewModel by sharedViewModel()

    private val prefs: Prefs by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSettingsHomeBinding>(
            inflater, R.layout.fragment_settings_home, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this

        view.refresh.isEnabled = false

        return view
    }

    override fun onStart() {
        btnUser.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsUserFragment
            )
        )

        btnAbout.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsAboutFragment
            )
        )

        btnChangePhoneNumber.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangePhoneNumberFragment
            )
        )


        btnChangeEmail.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangeEmail
            )
        )

        btnChangePassword.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangePasswordFragment
            )
        )

        btnManageSessions.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsManageSessionsFragment
            )
        )

        btnManageCalendar.setOnClickListener {
            val bottomSheetFragment = ManageCalendarTokenBottomSheetDialog()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        btnTheme.setOnClickListener {

            var selection = ""

            val b = AlertDialog.Builder(activity!!)
            b.setTitle(getString(R.string.theme))
            val checkedItem = themeOptions.indexOf(prefs.THEME!!)

            b.setSingleChoiceItems(themeOptions, checkedItem) { _, which ->
                selection = themeOptions[which]
            }

            b.setPositiveButton(getString(R.string.ok)) { _, _ ->
                if(selection.isNotBlank())
                    prefs.THEME = selection

                Log.i("Theme attached:", "${prefs.THEME}")
                when(prefs.THEME){
                    themeOptions[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    themeOptions[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    themeOptions[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            b.create().show()
        }

        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings)
    }

}

val themeOptions = arrayOf("Default", "Light", "Dark")
