package com.bke.datepoll.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.data.model.Booking

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

        if(b.workerId == null){
            holder.tvReportDuty.visibility = View.INVISIBLE
            holder.btnReportDuty.visibility = View.VISIBLE

            holder.btnReportDuty.setOnClickListener {
                holder.btnReportDuty.visibility = View.INVISIBLE
                holder.pbReportDuty.visibility = View.VISIBLE
                holder.btnEmergencyDuty.isEnabled = false
            }
        } else {
            holder.btnReportDuty.visibility = View.INVISIBLE
            holder.tvReportDuty.visibility = View.VISIBLE
            holder.tvReportDuty.text = b.workerName
        }

        if(b.emergencyWorkerId == null){
            holder.tvEmergencyDuty.visibility = View.INVISIBLE
            holder.btnEmergencyDuty.visibility = View.VISIBLE

            holder.btnEmergencyDuty.setOnClickListener {
                holder.pbReportEmergencyDuty.visibility = View.VISIBLE
                holder.btnEmergencyDuty.visibility = View.INVISIBLE
                holder.btnReportDuty.isEnabled = false
            }
        } else {
            holder.btnEmergencyDuty.visibility = View.INVISIBLE
            holder.tvEmergencyDuty.visibility = View.VISIBLE
            holder.tvEmergencyDuty.text = b.emergencyWorkerName
        }
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

    val btnReportDuty: Button = view.findViewById(R.id.btnReportDuty)
    val tvReportDuty: TextView = view.findViewById(R.id.tvReportDuty)
    val pbReportDuty: ProgressBar = view.findViewById(R.id.pbDuty)


    val btnEmergencyDuty: Button = view.findViewById(R.id.btnReportEmergencyDuty)
    val tvEmergencyDuty: TextView = view.findViewById(R.id.tvReportEmergencyDuty)
    val pbReportEmergencyDuty: ProgressBar = view.findViewById(R.id.pbEmergency)
}
