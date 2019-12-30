package com.bke.datepoll

import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    @JvmStatic
    fun stringToDate(
            value: String
    ): Long {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(value)
        return date.time
    }

    @JvmStatic
    @InverseMethod("stringToDate")
    fun dateToString(
            value: Long
    ): String {
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(Date(value))
    }
}