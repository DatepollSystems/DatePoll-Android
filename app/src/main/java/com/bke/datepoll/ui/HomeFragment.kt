package com.bke.datepoll.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentHomeBinding
import com.bke.datepoll.vm.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HomeFragment : Fragment() {

    private val vm: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        )
        val view = binding.root
        //here data must be an instance of the class MarsDataProvider
        binding.vm = vm
        binding.lifecycleOwner = this

        Log.i(vm.user.value?.toString(), "test")
        return view
    }
}
