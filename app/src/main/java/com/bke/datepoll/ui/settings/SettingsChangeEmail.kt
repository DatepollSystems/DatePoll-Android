package com.bke.datepoll.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentSettingsChangeEmailBinding
import com.bke.datepoll.databinding.FragmentSettingsChangePhoneNumberBinding
import com.bke.datepoll.vm.SettingsEmailViewModel
import kotlinx.android.synthetic.main.fragment_settings_change_email.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SettingsChangeEmail : Fragment() {

    val vm: SettingsEmailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSettingsChangeEmailBinding>(
            inflater, R.layout.fragment_settings_change_email, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this

        val adapter = EmailAdapter()
        binding.emails.adapter = adapter

        vm.emails.value?.let {
            adapter.data = LinkedList(it)
        }

        vm.emails.observe(this, Observer {
            it?.let {
                adapter.data = LinkedList(it)
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.change_email)
    }
}
