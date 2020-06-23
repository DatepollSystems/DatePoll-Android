package com.datepollsystems.datepoll

import android.annotation.SuppressLint
import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun stringToDate(
            value: String
    ): Long {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(value)!!
        return date.time
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @InverseMethod("stringToDate")
    fun dateToString(
            value: Long
    ): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(Date(value))
    }


}