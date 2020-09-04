package com.datepollsystems.datepoll.components.main.event

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel
import com.datepollsystems.datepoll.utils.formatDateEnToLocal


@BindingAdapter("setEventStartDate")
fun TextView.setStartDate(model: EventDbModel?) {
    model?.let {
        text = formatDateEnToLocal(model.startDate)
    }
}

@BindingAdapter("setEventIconDecisionColor")
fun ImageView.setEventIconDecisionColor(eventDbModel: EventDbModel?) {
    eventDbModel?.let {
        it.userDecision?.let { decision ->
            backgroundTintList = ColorStateList.valueOf(Color.parseColor(decision.color))
        }
    }
}