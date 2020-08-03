package com.datepollsystems.datepoll.components.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.datepollsystems.datepoll.databinding.FragmentFtueSuccessfulBinding
import com.datepollsystems.datepoll.components.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        //view.motionLayout.progre

        //lifecycleScope.launch(Dispatchers.Main){
        //    binding.motionLayout.
        //}


        return view
    }

    override fun onStart() {
        super.onStart()
        binding.motionLayout.transitionToEnd()

       viewLifecycleOwner.lifecycleScope.launch {
           delay(1500)
           startActivity(Intent(requireActivity(), MainActivity::class.java))
       }
    }
}