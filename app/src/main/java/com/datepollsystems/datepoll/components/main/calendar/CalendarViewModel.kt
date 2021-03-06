package com.datepollsystems.datepoll.components.main.calendar

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applandeo.materialcalendarview.EventDay
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.BirthdayDbModel
import com.datepollsystems.datepoll.data.BirthdayDto
import com.datepollsystems.datepoll.utils.toCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel : ViewModel() {
    val tag = "CalendarViewModel"
    val calendarEntries = MutableLiveData<List<EventDay>>(ArrayList())


    @SuppressLint("SimpleDateFormat")
    fun fillCalendar(b: List<BirthdayDbModel>) {
        viewModelScope.launch(Dispatchers.Main) {
            val list = ArrayList<EventDay>()
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

            for (element in b) {
                element.date.let {
                    list.add(
                        EventDay(
                            (formatter.parse(it) as Date).toCalendar(),
                            R.drawable.ic_cake_red_24dp
                        )
                    )
                }
            }

            calendarEntries.postValue(list)
        }
    }

}