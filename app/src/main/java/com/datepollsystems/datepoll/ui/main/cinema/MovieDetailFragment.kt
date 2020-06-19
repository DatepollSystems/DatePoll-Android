package com.datepollsystems.datepoll.ui.main.cinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentMovieDetailBinding
import com.datepollsystems.datepoll.databinding.FragmentMovieDetailBindingImpl
import com.datepollsystems.datepoll.vm.CinemaViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    val binding: FragmentMovieDetailBinding
        get() = _binding!!

    val cinemaViewModel: CinemaViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding.vm = cinemaViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}