package com.datepollsystems.datepoll.utils

import android.view.View
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.components.main.event.model.EventDateDbModel
import com.datepollsystems.datepoll.components.main.event.model.EventDateDto
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDbModel
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDto
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

fun List<EventDecisionDto>.transformInDbModelList(): List<EventDecisionDbModel> {
    return if (this.isNotEmpty()) {
        val l = ArrayList<EventDecisionDbModel>()
        for (i in this) {
            l.add(
                EventDecisionDbModel(
                    id = i.id,
                    eventId = i.eventId,
                    shownInCalendar = i.showInCalendar,
                    decision = i.decision
                )
            )
        }

        l
    } else {
        ArrayList()
    }
}

fun List<EventDateDto>.transformInDbModelList(eventId: Int): List<EventDateDbModel> {
    return if (this.isNotEmpty()) {
        val l = ArrayList<EventDateDbModel>()
        for(i in this){
            l.add(
                EventDateDbModel(
                    id = i.id,
                    eventId = eventId,
                    date = i.date,
                    description = i.description,
                    x = i.x,
                    y = i.y
                )
            )
        }

        l
    } else {
        ArrayList()
    }
}

fun Date.toCalendar(): Calendar{
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun showMainSnack(view: View, t: String, i: Int) {
    try {
        val s = Snackbar.make(view, t, i)
        s.setAnchorView(R.id.bottom_navigation)
        s.show()
    } catch (ex: Exception) {
        Timber.e("Not able to show main snack")
    }
}