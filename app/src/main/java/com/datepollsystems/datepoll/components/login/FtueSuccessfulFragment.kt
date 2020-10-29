package com.datepollsystems.datepoll.components.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.datepollsystems.datepoll.components.AppViewModel
import com.datepollsystems.datepoll.databinding.FragmentFtueSuccessfulBinding
import com.datepollsystems.datepoll.components.main.MainActivity
import com.datepollsystems.datepoll.networkModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.ext.scope

class FtueSuccessfulFragment : Fragment() {

    private var _binding: FragmentFtueSuccessfulBinding? = null
    private val binding: FragmentFtueSuccessfulBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFtueSuccessfulBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onStart() {
        super.onStart()

        unloadKoinModules(networkModule)
        loadKoinModules(networkModule)

        val appViewModel: AppViewModel by viewModel()

        appViewModel.apiData.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            }
        })
        appViewModel.loadApiInfo()

        binding.motionLayout.transitionToEnd()
    }
}