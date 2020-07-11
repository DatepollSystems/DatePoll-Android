package com.datepollsystems.datepoll.ui.main.cinema

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.datepollsystems.datepoll.R

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, profileImage: String) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.setStyle(CircularProgressDrawable.LARGE)
    circularProgressDrawable.start()

    Glide.with(view.context)
        .load(profileImage)
        .transition(GenericTransitionOptions.with(R.anim.fragment_fade_enter))
        .centerCrop()
        .into(view)
}