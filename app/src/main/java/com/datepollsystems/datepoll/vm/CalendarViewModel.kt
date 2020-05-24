package com.datepollsystems.datepoll.vm

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.applandeo.materialcalendarview.EventDay
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Birthday
import com.datepollsystems.datepoll.toCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel : BaseViewModel() {
    val tag = "CalendarViewModel"
    val calendarEntries = MutableLiveData<List<EventDay>>(ArrayList())


    @SuppressLint("SimpleDateFormat")
    fun fillCalendar(b: List<Birthday>) {
        viewModelScope.launch(Dispatchers.Main) {
            val list = ArrayList<EventDay>()
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

            for (element in b) {
                element.date?.let {
                    list.add(
                        EventDay(
                            (formatter.parse(it) as Date).toCalendar(),
                            R.drawable.ic_cake_black_24dp,
                            Color.parseColor("#cc4d04")
                        )
                    )
                }
            }

            calendarEntries.postValue(list)
        }
    }

}