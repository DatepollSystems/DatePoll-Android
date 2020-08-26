package com.datepollsystems.datepoll.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
private var formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

@SuppressLint("SimpleDateFormat")
private var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")

/**
 * @param date -> to date as a string formatted in yyyy-mm-dd
 * @return the date formatted in the android local date format depending on the system location settings
 *
 **/
fun formatDateEnToLocal(context: Context, dateStr: String): String {
    val date = formatter.parse(dateStr)
    return DateFormat.getDateInstance(DateFormat.LONG).format(date!!)
}