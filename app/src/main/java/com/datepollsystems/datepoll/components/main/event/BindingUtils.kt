package com.datepollsystems.datepoll.components.main.event

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.datepollsystems.datepoll.R
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
        backgroundTintList = if (it.userDecision != null)
            ColorStateList.valueOf(Color.parseColor(it.userDecision.color))
        else
            ColorStateList.valueOf(this.context.getColor(R.color.blueGrey))

    }
}

@BindingAdapter("setVisibilityWhenAlreadyVoted")
fun ImageView.setVisibilityWhenAlreadyVoted(eventDbModel: EventDbModel?) {
    eventDbModel?.let {
        visibility = if (it.alreadyVoted)
            View.INVISIBLE
        else
            View.VISIBLE
    }
}