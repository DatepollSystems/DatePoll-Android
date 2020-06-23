package com.datepollsystems.datepoll

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

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
