package com.datepollsystems.datepoll.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
private var formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

@SuppressLint("SimpleDateFormat")
private var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")

/**
 * @param   date -> to date as a string formatted in yyyy-mm-dd
 * @return  the date formatted in the android local date format depending on the system location setting
 *          return blank String if something isn't right
 **/
fun formatDateEnToLocal(dateStr: String): String {
    Timber.i("Try to format following date: $dateStr")
    if (!dateStr.isBlank()) {
        val date = formatter.parse(dateStr)
        return DateFormat.getDateInstance(DateFormat.LONG).format(date!!)
    }
    Timber.e("Formatting of Date ($dateStr) failed")
    return ""
}

fun formatDateToLocal(date: Date): String {
    Timber.i("Try to format following date: $date")
    return DateFormat.getDateInstance(DateFormat.LONG).format(date)
}

fun formatDateToEn(date: Date): String {
    Timber.i("Try to format following date: $date")
    return formatter.format(date)
}
