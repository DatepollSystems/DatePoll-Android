package com.datepollsystems.datepoll.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.repos.ENetworkState
import com.datepollsystems.datepoll.vm.EventViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_event.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class EventFragment : Fragment() {

    val vm: EventViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =
            inflater.inflate(R.layout.fragment_event, container, false)

        val eventCardAdapter = EventCardAdapter(vm)
        eventCardAdapter.data = ArrayList()
        view.eventsRecyclerView.adapter = eventCardAdapter

        view.connectionView.visibility = View.INVISIBLE

        view.eventFragmentSwipeToRefresh.setOnRefreshListener {
            vm.loadEventData(force = true)
        }

        initStateObserver(view)

        vm.decisions.observe(viewLifecycleOwner, Observer {
            it?.let {
                val bottomSheetFragment = VoteBottomSheetDialog(it, vm.decisionClickResult)
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
                        Log.i(tag, "Reload events and data")
                        view.eventFragmentSwipeToRefresh.isRefreshing = true
                    }
                    ENetworkState.ERROR -> {
                        Log.e(tag, "Something went wrong during event refresh")
                        view.eventsRecyclerView.visibility = View.INVISIBLE
                        view.connectionView.visibility = View.VISIBLE
                        view.eventFragmentSwipeToRefresh.isRefreshing = false

                    }
                    ENetworkState.DONE -> {
                        Log.i(tag, "Displaying events")
                        view.eventsRecyclerView.visibility = View.VISIBLE
                        view.connectionView.visibility = View.INVISIBLE
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
