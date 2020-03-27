package com.bke.datepoll.ui.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bke.datepoll.R
import com.bke.datepoll.database.model.event.EventDbModel
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.vm.EventViewModel
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.fragment_event.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.ArrayList


class EventFragment : Fragment() {

    val vm: EventViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_event, container, false)

        val eventCardAdapter = EventCardAdapter()
        eventCardAdapter.data = ArrayList()
        view.eventsRecyclerView.adapter = eventCardAdapter

        vm.loadEventsState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    ENetworkState.LOADING -> {
                        Log.i(tag, "Reload events and data")
                    }
                    ENetworkState.ERROR -> {
                        Log.e(tag, "Something went wrong during event refresh")
                    }
                    ENetworkState.DONE -> {
                        Log.i(tag, "Displaying events")
                    }
                }
            }
        })

        vm.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                eventCardAdapter.data = ArrayList(it)
            }
        })

        return view
    }

    override fun onStart() {

        //TODO SwipeToRefresh
        vm.loadEventData()

        super.onStart()
    }
}
