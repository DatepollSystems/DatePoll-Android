package com.bke.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentSettingsChangePhoneNumberBinding
import com.bke.datepoll.vm.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class SettingsChangePhoneNumberFragment : Fragment() {

    private val vm: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsChangePhoneNumberBinding>(
            inflater, R.layout.fragment_settings_change_phone_number, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this

        val adapter = PhoneNumberAdapter()
        binding.phoneNumbers.adapter = adapter

        vm.phoneNumbers.value?.let {
            adapter.data = LinkedList(it)
        }

        vm.phoneNumbers.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = LinkedList(it)
            }
        })

        return view
    }
}
