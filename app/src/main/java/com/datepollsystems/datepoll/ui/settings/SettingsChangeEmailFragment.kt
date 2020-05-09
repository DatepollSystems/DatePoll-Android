package com.datepollsystems.datepoll.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsChangeEmailBinding
import com.datepollsystems.datepoll.databinding.FragmentSettingsChangePhoneNumberBinding
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.vm.SettingsEmailViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SettingsChangeEmailFragment : Fragment() {

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

        val adapter = EmailAdapter(requireActivity(), vm)
        binding.emails.adapter = adapter

        vm.emails.value?.let {
            adapter.data = LinkedList(it)
        }

        vm.emails.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = LinkedList(it)
            }
        })

        vm.saveEmailsState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    ENetworkState.DONE -> {
                        Snackbar.make(view, getString(R.string.saved_successfully), Snackbar.LENGTH_LONG).show()
                    }
                    ENetworkState.LOADING -> {

                    }
                    ENetworkState.ERROR -> {
                        Snackbar.make(view, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        binding.button.setOnClickListener {
            val e = binding.tiEmail.editText?.text.toString()

            if(e.isNotBlank()){
                binding.tiEmail.error = ""
                binding.tiEmail.editText?.setText("")
                vm.addEmail(e)
            } else {
                binding.tiEmail.error = getString(R.string.field_is_empty)
            }
        }

        binding.btnSave.setOnClickListener {
            vm.saveEmails()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.change_email)
    }
}
