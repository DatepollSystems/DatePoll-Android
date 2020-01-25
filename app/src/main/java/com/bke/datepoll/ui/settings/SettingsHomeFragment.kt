package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentSettingsHomeBinding
import com.bke.datepoll.vm.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsHomeFragment : Fragment() {

    private val vm: SettingsViewModel by sharedViewModel()

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


        return view
    }

    override fun onStart() {
        btnUserSettings.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsUserFragment
            )
        )

        btnSettingsAbout.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsAboutFragment
            )
        )

        btnSettingsChangePhoneNumber.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangePhoneNumberFragment
            )
        )

        btnSettingsChangeEmail.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_settingsHomeFragment_to_settingsChangeEmail
            )
        )

        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings)
    }

}
