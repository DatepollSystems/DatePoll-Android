package com.datepollsystems.datepoll.components.settings

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
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsHomeBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.components.main.MainViewModel
import com.datepollsystems.datepoll.databinding.FragmentSettingsAboutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class SettingsHomeFragment : Fragment() {

    private val vm: SettingsViewModel by sharedViewModel()

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val prefs: Prefs by inject()

    private var bottomSheetFragment: ManageCalendarTokenBottomSheetDialog? = null

    private var _binding: FragmentSettingsHomeBinding? = null
    val binding: FragmentSettingsHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsHomeBinding.inflate(layoutInflater)
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this

        binding.refresh.isEnabled = false

        vm.calendarSessionTokenState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.DONE -> {
                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
                        bottomSheetFragment?.show(parentFragmentManager, bottomSheetFragment?.tag)
                    }

                    ENetworkState.LOADING -> {
                        binding.refresh.isEnabled = true
                        binding.refresh.isRefreshing = true
                    }

                    ENetworkState.ERROR -> {
                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
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
                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    ENetworkState.LOADING -> {
                        binding.refresh.isRefreshing = true
                        binding.refresh.isEnabled = true
                    }

                    ENetworkState.DONE -> {
                        binding.refresh.isRefreshing = false
                        binding.refresh.isEnabled = false
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
        binding.btnUser.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsUserFragment
            )
        )

        binding.btnAbout.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsAboutFragment
            )
        )

        binding.btnChangePhoneNumber.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangePhoneNumberFragment
            )
        )


        binding.btnChangeEmail.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangeEmail
            )
        )

        binding.btnChangePassword.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangePasswordFragment
            )
        )

        binding.btnManageSessions.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsManageSessionsFragment
            )
        )

        binding.btnManageCalendar.setOnClickListener {
            bottomSheetFragment = ManageCalendarTokenBottomSheetDialog(vm.calendarSessionToken)
            vm.getCalendarToken()
        }

        binding.btnChangeIfShownInBirthdayList.setOnClickListener (
            Navigation.createNavigateOnClickListener(
                R.id.action_nav_settings_to_settingsChangeIfShownInBirthdayList
            )
        )

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_dialog_desc)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    mainViewModel.logout()
                }
                .setNegativeButton(android.R.string.cancel, null).create().show()
        }

        binding.btnLicences.setOnClickListener (
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsLicenceFragment
            )
        )

        binding.btnTheme.setOnClickListener {

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

                Timber.i("Theme attached: ${prefs.theme}")
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
