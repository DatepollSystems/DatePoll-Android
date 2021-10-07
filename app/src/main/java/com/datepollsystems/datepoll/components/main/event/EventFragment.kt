package com.datepollsystems.datepoll.components.main.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.components.main.home.VoteBottomSheetDialog
import com.datepollsystems.datepoll.databinding.FragmentEventBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class EventFragment : Fragment() {

    val vm: EventViewModel by sharedViewModel()

    private var _binding: FragmentEventBinding? = null
    val binding: FragmentEventBinding
        get() = _binding!!

    private var _adapter: EventAdapter? = null
    private val eventAdapter: EventAdapter
        get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEventBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner

        _adapter = EventAdapter(
            EventClickListener {
                //event click -> open event details

            },
            EventClickListener {
                //event decision click
                vm.loadDecisionsForEvent(it.id)
                loading()
            }, EventClickListener {
                //event long click -> action mode, delete answer
                val bottomSheet = EventActionsBottomSheet(it)
                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
            })

        eventAdapter.submitList(vm.events.value)

        //Observers for updating recycler view
        vm.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!vm.filterChecked.value!!)
                    eventAdapter.submitList(it)
            }
        })

        vm.filteredEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (vm.filterChecked.value!!)
                    eventAdapter.submitList(it)
            }
        })

        binding.eventsRecyclerView.adapter = eventAdapter

        binding.eventFragmentSwipeToRefresh.setOnRefreshListener {
            vm.loadEventData(true)
        }

        initStateObserver(binding.root)

        vm.decisions.observe(viewLifecycleOwner, Observer {
            it?.let {
                val bottomSheetFragment =
                    VoteBottomSheetDialog(
                        it,
                        vm.decisionClickResult
                    )
                activity?.let { a ->
                    bottomSheetFragment.show(a.supportFragmentManager, bottomSheetFragment.tag)
                }
                loading(false)
                vm.decisions.postValue(null)
            }
        })

        vm.decisionClickResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                vm.voteForEvent(it)
            }
            vm.decisionClickResult.postValue(null)
        })


        return binding.root
    }

    private fun initStateObserver(view: View) {

        vm.makeDecisionState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.LOADING -> {
                        loading()
                    }
                    ENetworkState.ERROR -> {
                        loading(false)
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    ENetworkState.DONE -> {
                        loading(false)
                    }
                }

                vm.makeDecisionState.postValue(null)
            }
        })

        vm.removeVoteForEventState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.LOADING -> {
                        loading()
                    }
                    ENetworkState.ERROR -> {
                        loading(false)
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    ENetworkState.DONE -> {
                        loading(false)
                    }
                }

                vm.removeVoteForEventState.postValue(null)
            }
        })

        vm.loadEventsState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.LOADING -> {
                        Timber.i("Reload events and data")
                        loading()
                    }
                    ENetworkState.ERROR -> {
                        Timber.e("Something went wrong during event refresh")
                        binding.eventsRecyclerView.visibility = View.INVISIBLE
                        loading(false)

                    }
                    ENetworkState.DONE -> {
                        Timber.i("Displaying events")
                        binding.eventsRecyclerView.visibility = View.VISIBLE
                        loading(false)
                    }
                }

                vm.loadEventsState.postValue(null)
            }
        })

        vm.changeList.observe(viewLifecycleOwner, Observer { checked ->
            checked?.let {
                if (checked) // show all
                    eventAdapter.submitList(vm.filteredEvents.value)
                else
                    eventAdapter.submitList(vm.events.value)

                vm.changeList.postValue(null)
            }
        })
    }

    override fun onStart() {
        vm.loadEventData()
        super.onStart()
    }

    private fun loading(loading: Boolean = true) {
        binding.eventFragmentSwipeToRefresh.isRefreshing = loading
    }
}
