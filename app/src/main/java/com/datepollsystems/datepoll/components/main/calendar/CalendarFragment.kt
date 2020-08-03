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
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarFragment : Fragment() {

    val prefs: Prefs by inject()
    val vm: CalendarViewModel by viewModel()
    val main: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onStart() {
        super.onStart()

        val cal = calendarView
        val swipeToRefresh = calendarSwipeToRefresh

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
