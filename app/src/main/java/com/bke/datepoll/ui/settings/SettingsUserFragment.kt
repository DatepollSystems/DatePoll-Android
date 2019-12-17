package com.bke.datepoll.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentHomeBinding
import com.bke.datepoll.databinding.FragmentSettingsUserBinding
import com.bke.datepoll.vm.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings_user.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsUserFragment : Fragment() {

    val vm: SettingsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentSettingsUserBinding>(
            inflater, R.layout.fragment_settings_user, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this


        return view
    }


    override fun onStart() {

        userSettingsSwipeRefresh.setOnRefreshListener {
            Snackbar.make(this.requireView(), "Test", Snackbar.LENGTH_LONG).show()
            userSettingsSwipeRefresh.isRefreshing = false
        }
        super.onStart()
    }

}
