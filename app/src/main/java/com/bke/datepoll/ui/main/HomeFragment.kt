package com.bke.datepoll.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentHomeBinding
import com.bke.datepoll.repos.ENetworkState
import com.bke.datepoll.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.card_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class HomeFragment : Fragment() {

    private val vm: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        )
        val view = binding.root

        binding.vm = vm
        binding.lifecycleOwner = this

        view.connectionView.visibility = View.INVISIBLE

        val adapter = CardAdapter(activity as AppCompatActivity)
        view.cardList.adapter = adapter
        setupObservers(view, adapter)
        adapter.notifyDataSetChanged()
        view.swipeToRefresh.setOnRefreshListener {
            vm.loadHomepage()
        }

        vm.loadHomepage()
        return view
    }

    private fun setupObservers(view: View, mainAdapter: CardAdapter) {

        vm.loadHomepageState.observe(viewLifecycleOwner, Observer {
            it?.let {
                val s = swipeToRefresh

                when (it) {
                    ENetworkState.LOADING -> s.isRefreshing = true
                    ENetworkState.DONE -> {
                        s.isRefreshing = false
                        cardList.visibility = View.VISIBLE
                        connectionView.visibility = View.INVISIBLE
                    }
                    ENetworkState.ERROR -> {
                        s.isRefreshing = false
                        cardList.visibility = View.INVISIBLE
                        connectionView.visibility = View.VISIBLE
                    }
                }

                vm.loadHomepageState.postValue(null)
            }
        })

        vm.bookings.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainAdapter.bookingsData = LinkedList(it)
            }
        })

        vm.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainAdapter.eventsData = LinkedList(it)
            }
        })

        vm.birthdays.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainAdapter.birthdayData = LinkedList(it)
            }
        })

    }
}
