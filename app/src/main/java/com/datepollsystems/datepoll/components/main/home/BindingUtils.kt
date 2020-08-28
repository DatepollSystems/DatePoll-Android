package com.datepollsystems.datepoll.components.main.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.BirthdayDbModel
import com.datepollsystems.datepoll.utils.formatDateEnToLocal
import org.w3c.dom.Text

@BindingAdapter("setDateFormatted")
fun TextView.setDateFormatted(date: String?) {
    date?.let {
        text = formatDateEnToLocal(date)
    }
}

@BindingAdapter("tickets")
fun TextView.tickets(tickets: Int?){
    tickets?.let {
        text = context.getString(R.string.you_booked_x_tickets, it.toString())
    }
}