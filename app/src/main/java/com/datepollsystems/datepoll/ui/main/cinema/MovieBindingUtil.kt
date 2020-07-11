package com.datepollsystems.datepoll.ui.main.cinema

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, profileImage: String) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.setStyle(CircularProgressDrawable.LARGE)
    circularProgressDrawable.start()

    Glide.with(view.context)
        .load(profileImage)
        .centerCrop()
        .into(view)
}