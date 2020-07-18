package com.datepollsystems.datepoll.components.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.NewPhoneNumberRequest
import com.datepollsystems.datepoll.databinding.FragmentSettingsChangePhoneNumberBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.google.android.material.snackbar.Snackbar
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

        val adapter = PhoneNumberAdapter(vm)
        binding.phoneNumbers.adapter = adapter

        vm.phoneNumbers.value?.let {
            adapter.data = LinkedList(it)
        }

        vm.phoneNumbers.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = LinkedList(it)
            }
        })

        initStateObservers(view)


        binding.button.setOnClickListener {
            val label = binding.tiLabel.editText?.text.toString()
            val number = binding.tiPhoneNumber.editText?.text.toString()

            if(label.isNotBlank() && number.isNotBlank())
                vm.addPhoneNumber(
                    NewPhoneNumberRequest(
                        label,
                        number
                    )
                )

        }

        return view
    }

    private fun initStateObservers(v: View){
        vm.removeAddPhoneNumberState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it) {
                    ENetworkState.ERROR -> Snackbar.make(v, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show()
                    ENetworkState.LOADING -> null
                    ENetworkState.DONE -> null
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.change_phone_number)
    }
}
