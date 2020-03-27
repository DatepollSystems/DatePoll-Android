package com.bke.datepoll.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.database.model.event.EventDbModel
import java.util.ArrayList

class EventCardAdapter : RecyclerView.Adapter<EventCardViewHolder>() {

    var data = ArrayList<EventDbModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: EventCardViewHolder, position: Int) {

        val item = data[position]

        holder.apply {
            tvTitle.text = item.name
            tvDescription.text = item.description
            tvStartDate.text = item.startDate
            tvEndDate.text = item.endDate
            btnAnswer.setOnClickListener {

            }
            btnInfo.setOnClickListener {

            }

            item.userDecision?.let {
                tvAnswer.visibility = View.VISIBLE
                tvAnswerTitle.visibility = View.VISIBLE
                tvAnswer.text = it.decision
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return EventCardViewHolder(
            layoutInflater.inflate(
                R.layout.event_card,
                parent,
                false
            )
        )
    }
}

class EventCardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    val tvStartDate: TextView = view.findViewById(R.id.tvStartDate)
    val tvEndDate: TextView = view.findViewById(R.id.tvEndDate)
    val tvAnswer: TextView = view.findViewById(R.id.tvAnswer)
    val tvAnswerTitle: TextView = view.findViewById(R.id.tvAnswerTitle)

    val btnInfo: Button = view.findViewById(R.id.btnInfo)
    val btnAnswer: Button = view.findViewById(R.id.btnAnswer)
}
