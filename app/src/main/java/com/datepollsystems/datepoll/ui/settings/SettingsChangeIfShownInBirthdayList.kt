package com.datepollsystems.datepoll.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentSettingsChangeIfShownInBirthdayListBinding
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.vm.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class SettingsChangeIfShownInBirthdayList : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()

    private var _binding: FragmentSettingsChangeIfShownInBirthdayListBinding? = null
    val binding: FragmentSettingsChangeIfShownInBirthdayListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsChangeIfShownInBirthdayListBinding.inflate(
            inflater,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm


        vm.getShownInBirthdayListState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    ENetworkState.LOADING -> {
                        binding.isBirthdayShown.isEnabled = false
                        isLoading()
                    }
                    ENetworkState.DONE -> {
                        isLoading(false)
                        binding.isBirthdayShown.isEnabled = true
                    }
                    ENetworkState.ERROR -> {
                        isLoading(false)
                        binding.isBirthdayShown.isEnabled = true
                        Snackbar.make(binding.root, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        vm.checkIfBirthdayIsShown()

        return binding.root
    }


    private fun isLoading(v: Boolean = true) {
        if (v)
            binding.loading.visibility = View.VISIBLE
        else
            binding.loading.visibility = View.INVISIBLE
    }
}