package com.datepollsystems.datepoll.components.main.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.components.main.home.VoteBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_event.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import kotlin.collections.ArrayList


class EventFragment : Fragment() {

    val vm: EventViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =
            inflater.inflate(R.layout.fragment_event, container, false)

        val eventCardAdapter =
            EventCardAdapter(vm)
        eventCardAdapter.data = ArrayList()
        view.eventsRecyclerView.adapter = eventCardAdapter

        view.eventFragmentSwipeToRefresh.setOnRefreshListener {
            vm.loadEventData(force = true)
        }

        /**
        view.switch1.setOnCheckedChangeListener { _, isChecked ->
            vm.filterChecked = isChecked
            if(isChecked){
                vm.events.value?.let {
                    eventCardAdapter.data = ArrayList(it)
                }
            } else {
                vm.filteredEvents.value?.let {
                    eventCardAdapter.data = ArrayList(it)
                }
            }
        }**/


        initStateObserver(view)

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
                vm.decisions.postValue(null)
            }
        })

        vm.decisionClickResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                vm.voteForEvent(it)
            }
            vm.decisionClickResult.postValue(null)
        })

        vm.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(vm.filterChecked)
                    eventCardAdapter.data = ArrayList(it)
            }
        })

        vm.filteredEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!vm.filterChecked)
                    eventCardAdapter.data = ArrayList(it)
            }
        })

        return view
    }

    private fun initStateObserver(view: View) {

        vm.makeDecisionState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.LOADING -> {
                        view.eventFragmentSwipeToRefresh.isRefreshing = true
                    }
                    ENetworkState.ERROR -> {
                        view.eventFragmentSwipeToRefresh.isRefreshing = false
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    ENetworkState.DONE -> {
                        view.eventFragmentSwipeToRefresh.isRefreshing = false
                    }
                }

                vm.makeDecisionState.postValue(null)
            }
        })

        vm.removeVoteForEventState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ENetworkState.LOADING -> {
                        view.eventFragmentSwipeToRefresh.isRefreshing = true
                    }
                    ENetworkState.ERROR -> {
                        view.eventFragmentSwipeToRefresh.isRefreshing = false
                        Snackbar.make(
                            view,
                            getString(R.string.something_went_wrong),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    ENetworkState.DONE -> {
                        view.eventFragmentSwipeToRefresh.isRefreshing = false
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
                        view.eventFragmentSwipeToRefresh.isRefreshing = true
                    }
                    ENetworkState.ERROR -> {
                        Timber.e("Something went wrong during event refresh")
                        view.eventsRecyclerView.visibility = View.INVISIBLE
                        view.eventFragmentSwipeToRefresh.isRefreshing = false

                    }
                    ENetworkState.DONE -> {
                        Timber.i("Displaying events")
                        view.eventsRecyclerView.visibility = View.VISIBLE
                        view.eventFragmentSwipeToRefresh.isRefreshing = false
                    }
                }

                vm.loadEventsState.postValue(null)
            }
        })
    }

    override fun onStart() {
        vm.loadEventData()
        super.onStart()
    }
}
