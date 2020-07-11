package com.datepollsystems.datepoll.ui.main.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.FragmentMovieDetailBinding
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.vm.CinemaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    val binding: FragmentMovieDetailBinding
        get() = _binding!!

    private val cinemaViewModel: CinemaViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding.vm = cinemaViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        cinemaViewModel.apply {
            applyForMovieWorkerDetailState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Applied for movie worker")
                }
            })
            applyForEmergencyMovieWorkerDetailState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Applied for emergency movie worker")
                }
            })
            unsubscribeOfMovieWorkerState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Unsubscribed of movie worker")
                }
            })
            unsubscribeOfEmergencyMovieWorkerState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Unsubscribed of emergency movie worker")
                }
            })
        }

        return binding.root
    }

    private fun movieStateHandler(it: ENetworkState, msg: String) {
        when (it) {
            ENetworkState.LOADING -> {
                binding.loading.visibility = View.VISIBLE
            }
            ENetworkState.DONE -> {
                binding.loading.visibility = View.INVISIBLE
                if (it.code == 200)
                    Timber.i("$msg successful")
            }
            ENetworkState.ERROR -> {
                binding.loading.visibility = View.INVISIBLE
                Snackbar.make(
                    requireView(),
                    getString(R.string.something_went_wrong),
                    Snackbar.LENGTH_LONG
                ).setAnchorView(activity?.bottom_navigation).show()
                Timber.e("$msg failed error-code: ${it.code}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().bottom_navigation?.visibility = View.GONE
    }
}