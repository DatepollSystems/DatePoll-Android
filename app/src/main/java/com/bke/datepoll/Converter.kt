package com.bke.datepoll

import android.widget.EditText
import androidx.databinding.InverseMethod
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    @JvmStatic
    fun stringToDate(
            view: TextInputEditText,
            value: String
    ): Long {
        val date = SimpleDateFormat("dd-MM-yyyy").parse(value)
        return date.time
    }

    @JvmStatic
    @InverseMethod("stringToDate")
    fun dateToString(
            view: TextInputEditText,
            value: Long
    ): String {
        var formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(Date(value))
    }
}