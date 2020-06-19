package com.datepollsystems.datepoll.ui.main.cinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentCinemaBinding
import com.datepollsystems.datepoll.vm.CinemaViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CinemaFragment : Fragment() {

    private var _binding: FragmentCinemaBinding? = null
    val binding: FragmentCinemaBinding
        get() = _binding!!

    private val cinemaViewModel: CinemaViewModel by sharedViewModel()

    lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCinemaBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = cinemaViewModel

        adapter = MovieAdapter(listOf(), binding.root, cinemaViewModel)

        setupObserver()

        binding.apply {
            moviesRecycler.adapter = adapter
        }

        cinemaViewModel.loadMovies()

        return binding.root
    }

    private fun setupObserver() {
        cinemaViewModel.apply {

            movies.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.data = it
                    Timber.i("Updated movies")
                }
            })

            loadMoviesState.observe(viewLifecycleOwner, Observer {

            })
        }
    }
}
