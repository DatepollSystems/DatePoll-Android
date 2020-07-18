package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Booking

class BookingAdapter : RecyclerView.Adapter<BookingViewHolder>() {
    var data = listOf<Booking>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val b = data[position]

        holder.tvMovieName.text = b.movieName
        holder.tvMovieDate.text = b.movieDate
        holder.tvBookedSeats.text = b.amount.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BookingViewHolder(
            layoutInflater.inflate(
                R.layout.booking_view_item,
                parent,
                false
            )
        )
    }
}

class BookingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvMovieName: TextView = view.findViewById(R.id.tvMovieItemName)
    val tvMovieDate: TextView = view.findViewById(R.id.tvMovieItemDate)
    val tvBookedSeats: TextView = view.findViewById(R.id.tvBookedSeats)
}
