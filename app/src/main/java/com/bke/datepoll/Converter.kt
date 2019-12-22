package com.bke.datepoll

import android.widget.EditText
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter

object Converter {

    @BindingConversion
    @JvmStatic
    fun intToStr(value: Int?): String? {
        // Important to break potential infinite loops.
        return value.toString()
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun captureIntValue(view: EditText?): Int? {
        var value: Long = 0
        try {
            value = view!!.text.toString().toInt().toLong()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return value.toInt()
    }
}