package com.datepollsystems.datepoll.components.main.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.main.MainViewModel
import com.datepollsystems.datepoll.components.main.cinema.CinemaViewModel
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.data.MovieOrder
import com.datepollsystems.datepoll.data.MovieOrderTupl
import com.datepollsystems.datepoll.data.MovieOrdersDto
import com.datepollsystems.datepoll.databinding.BookTicketBottomSheetBinding
import com.datepollsystems.datepoll.databinding.FragmentHomeBinding
import com.datepollsystems.datepoll.databinding.HomeMovieWorkerBottomSheetBinding
import com.datepollsystems.datepoll.utils.formatDateEnToLocal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val vm: MainViewModel by sharedViewModel()
    private val cinemaViewModel: CinemaViewModel by sharedViewModel()

    private var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(
            inflater, container, false
        )

        binding.vm = vm
        binding.lifecycleOwner = this

        setupBirthdayCard()
        setupBookingCard()
        setupMovieWorkerCard()

        binding.swipeToRefresh.setOnRefreshListener {
            vm.loadHomepage(force = true)
        }

        vm.loadHomepage()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().bottom_navigation?.visibility = View.VISIBLE
    }

    private fun setupMovieWorkerCard() {
        val adapter = MovieWorkerAdapter(MovieWorkerClickListener {
            val sheet = CinemaWorkerInfoBottomSheet(it.id)
            sheet.show(parentFragmentManager, sheet.tag)
        })
        vm.movieWorkerDetails.value?.let {
            adapter.submitList(it)
        }
        vm.movieWorkerDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.workerDetailsList.adapter = adapter
    }

    private fun setupBookingCard() {
        val bookingAdapter = BookingsAdapter(BookingAdapterClickListener {
            cinemaViewModel.detailMovie.postValue(it)
            findNavController().navigate(R.id.action_nav_home_to_movieDetailFragment)
        })

        vm.bookings.value?.let {
            bookingAdapter.submitList(it)
        }
        binding.bookingsList.adapter = bookingAdapter

        vm.bookings.observe(viewLifecycleOwner, Observer {
            it?.let {
                bookingAdapter.submitList(it)
            }
        })

        vm.loadBookedMoviesState.observe(viewLifecycleOwner, Observer {
            it?.let {
                val s = binding.swipeToRefresh

                when (it) {
                    ENetworkState.LOADING -> s.isRefreshing = true
                    ENetworkState.DONE -> {
                        s.isRefreshing = false
                    }
                    ENetworkState.ERROR -> {
                        if (it.code == 401) {
                            //User not authorized
                            vm.logout()
                        }
                        s.isRefreshing = false
                    }
                }

                vm.loadBookedMoviesState.postValue(null)
            }
        })
    }

    private fun setupBirthdayCard() {
        val birthdayAdapter = BirthdayAdapter()
        vm.birthdays.value?.let {
            birthdayAdapter.submitList(it)
        }
        binding.birthdayList.adapter = birthdayAdapter

        vm.birthdays.observe(viewLifecycleOwner, Observer {
            it?.let {
                birthdayAdapter.submitList(it)
            }
        })

        vm.loadBirthdaysAndBroadcastState.observe(viewLifecycleOwner, Observer {
            it?.let {
                val s = binding.swipeToRefresh

                when (it) {
                    ENetworkState.LOADING -> s.isRefreshing = true
                    ENetworkState.DONE -> {
                        s.isRefreshing = false
                    }
                    ENetworkState.ERROR -> {
                        if (it.code == 401) {
                            //User not authorized
                            vm.logout()
                        }
                        s.isRefreshing = false
                    }
                }

                vm.loadBirthdaysAndBroadcastState.postValue(null)
            }
        })
    }

}
