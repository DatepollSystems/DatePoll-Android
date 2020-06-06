package com.datepollsystems.datepoll.ui.settings

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
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsHomeBinding
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.vm.MainViewModel
import com.datepollsystems.datepoll.vm.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings_home.*
import kotlinx.android.synthetic.main.fragment_settings_home.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsHomeFragment : Fragment() {

    private val vm: SettingsViewModel by sharedViewModel()

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val prefs: Prefs by inject()

    private var bottomSheetFragment: ManageCalendarTokenBottomSheetDialog? = null

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

        vm.calendarSessionTokenState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.DONE -> {
                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                        bottomSheetFragment?.show(parentFragmentManager, bottomSheetFragment?.tag)
                    }

                    ENetworkState.LOADING -> {
                        refresh.isEnabled = true
                        refresh.isRefreshing = true
                    }

                    ENetworkState.ERROR -> {
                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                vm.calendarSessionTokenState.postValue(null)
            }
        })

        vm.calendarSessionTokenResetState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.ERROR -> {
                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    ENetworkState.LOADING -> {
                        refresh.isRefreshing = true
                        refresh.isEnabled = true
                    }

                    ENetworkState.DONE -> {
                        refresh.isRefreshing = false
                        refresh.isEnabled = false
                        Snackbar.make(
                            view,
                            "Calendar token successfully reset",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                vm.calendarSessionTokenResetState.postValue(null)
            }
        })

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
            bottomSheetFragment = ManageCalendarTokenBottomSheetDialog(vm.calendarSessionToken)
            vm.getCalendarToken()
        }

        btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_dialog_desc)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    mainViewModel.logout()
                }
                .setNegativeButton(android.R.string.no, null).create().show()
        }

        btnLicences.setOnClickListener (
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsLicenceFragment
            )
        )

        btnTheme.setOnClickListener {

            var selection = ""

            val b = AlertDialog.Builder(requireActivity())
            b.setTitle(getString(R.string.theme))
            val checkedItem = themeOptions.indexOf(prefs.theme!!)

            b.setSingleChoiceItems(themeOptions, checkedItem) { _, which ->
                selection = themeOptions[which]
            }

            b.setPositiveButton(getString(R.string.ok)) { _, _ ->
                if (selection.isNotBlank())
                    prefs.theme = selection

                Log.i("Theme attached:", "${prefs.theme}")
                when (prefs.theme) {
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
