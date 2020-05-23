package com.datepollsystems.datepoll.ui.main

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.applandeo.materialcalendarview.CalendarView
import com.datepollsystems.datepoll.Prefs

import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.ui.settings.themeOptions
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.android.ext.android.inject
import org.koin.ext.getScopeId

class CalendarFragment : Fragment() {

    val prefs: Prefs by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }


    override fun onStart() {
        super.onStart()
    }
}
