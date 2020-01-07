package com.bke.datepoll.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentHomeBinding
import com.bke.datepoll.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_settings_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HomeFragment : Fragment() {

    private val vm: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        return view
    }
}
