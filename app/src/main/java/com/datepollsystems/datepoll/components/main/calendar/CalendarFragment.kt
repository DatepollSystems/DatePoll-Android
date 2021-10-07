package com.datepollsystems.datepoll.components.main.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.datepollsystems.datepoll.core.Prefs
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.main.MainViewModel
import com.datepollsystems.datepoll.databinding.FragmentCalendarBinding
import com.datepollsystems.datepoll.databinding.FragmentEventBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarFragment : Fragment() {

    val prefs: Prefs by inject()
    val vm: CalendarViewModel by viewModel()
    val main: MainViewModel by sharedViewModel()

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()


        val cal = binding.calendarView
        val swipeToRefresh = binding.calendarSwipeToRefresh

        vm.calendarEntries.observe(viewLifecycleOwner, Observer {
            cal.setEvents(it)
        })

        main.birthdays.value?.let {
            vm.fillCalendar(it)
        }

        cal.setOnDayClickListener(object :
            OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {

            }
        })

        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
        }
    }
}
