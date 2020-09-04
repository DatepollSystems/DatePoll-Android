package com.datepollsystems.datepoll.utils

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel

@BindingAdapter("cardVisible")
fun setVisibilityOfCard(view: CardView, count: Int?) {
    count?.let {
        if(count > 0){
            view.visibility =  View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}
