package com.datepollsystems.datepoll.components.main.cinema

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.BookTicketBottomSheetBinding
import com.datepollsystems.datepoll.databinding.FragmentMovieDetailBinding
import com.datepollsystems.datepoll.core.ENetworkState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_ftue_server_instance.loading
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    val binding: FragmentMovieDetailBinding
        get() = _binding!!

    private val cinemaViewModel: CinemaViewModel by sharedViewModel()

    private var sheet: BookBottomSheet? = null

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
                    applyForMovieWorkerDetailState.postValue(null)
                }
            })
            applyForEmergencyMovieWorkerDetailState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Applied for emergency movie worker")
                    applyForEmergencyMovieWorkerDetailState.postValue(null)
                }
            })
            unsubscribeOfMovieWorkerState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Unsubscribed of movie worker")
                    unsubscribeOfMovieWorkerState.postValue(null)
                }
            })
            unsubscribeOfEmergencyMovieWorkerState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Unsubscribed of emergency movie worker")
                    unsubscribeOfEmergencyMovieWorkerState.postValue(null)
                }
            })

            showBottomSheet.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it) {
                        sheet = BookBottomSheet()
                        sheet?.show(parentFragmentManager, sheet!!.tag)
                    } else {
                        sheet?.dismiss()
                    }
                }
            })
            bookTicketState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    when (it) {
                        ENetworkState.LOADING -> {
                            loading()
                        }
                        ENetworkState.DONE -> {
                            loading(false)
                            val dialog = AlertDialog.Builder(requireContext()).setTitle("Tickets")
                                .setMessage("You have booked ${cinemaViewModel.detailMovie.value!!.bookedTicketsForYourself}!")
                            dialog.show()
                        }
                        ENetworkState.ERROR -> {
                            loading(false)
                            Snackbar.make(
                                binding.root,
                                getString(R.string.something_went_wrong),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    bookTicketState.postValue(null)
                }
            })
            cancelTicketReservationState.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieStateHandler(it, "Canceled reservation")
                    cancelTicketReservationState.postValue(null)
                }
            })
        }

        return binding.root
    }

    private fun loading(valid: Boolean = true) {
        if (valid)
            loading.visibility = View.VISIBLE
        else
            loading.visibility = View.INVISIBLE
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
                ).show()
                Timber.e("$msg failed error-code: ${it.code}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().bottom_navigation?.visibility = View.GONE
    }

    class BookBottomSheet : BottomSheetDialogFragment() {

        private val vm: CinemaViewModel by sharedViewModel()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = BookTicketBottomSheetBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.vm = vm

            return binding.root
        }

        override fun onDismiss(dialog: DialogInterface) {
            super.onDismiss(dialog)
            vm.showBottomSheet.postValue(null)
        }
    }
}