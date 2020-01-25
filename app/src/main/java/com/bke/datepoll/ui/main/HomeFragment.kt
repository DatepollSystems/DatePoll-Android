package com.bke.datepoll.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentHomeBinding
import com.bke.datepoll.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_settings_home.*
import org.koin.android.ext.android.bind
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


        //Birthday List
        val birthdayAdapter = BirthdayAdapter()
        binding.birthdays.adapter = birthdayAdapter

        vm.birthdays.observe(viewLifecycleOwner, Observer {
            it?.let {
                birthdayAdapter.data = LinkedList(it)
            }
        })

        //Event List
        val eventAdapter = EventAdapter(activity as AppCompatActivity)
        binding.events.adapter = eventAdapter

        vm.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                eventAdapter.data = LinkedList(it)
            }
        })

        //Booking List
        val bookingAdapter = BookingAdapter()
        binding.bookings.adapter = bookingAdapter

        vm.bookings.observe(viewLifecycleOwner, Observer {
            it?.let {
                bookingAdapter.data = it
            }
        })


        return view
    }

    override fun onStart() {
        vm.loadHomepage()
        super.onStart()
    }
}
