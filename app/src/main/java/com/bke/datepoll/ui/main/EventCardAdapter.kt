package com.bke.datepoll.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.database.model.event.EventDbModel
import com.bke.datepoll.vm.EventViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class EventCardAdapter(private val viewModel: EventViewModel) :
    RecyclerView.Adapter<EventCardViewHolder>() {

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

            if (item.description.isBlank())
                tvDescription.visibility = View.GONE
            else {
                tvDescription.apply {
                    text = item.description
                    visibility = View.VISIBLE
                }
            }



            tvStartDate.text = item.startDate
            tvEndDate.text = item.endDate
            btnAnswer.setOnClickListener {
                if (btnAnswer.text == it.context.getString(R.string.answer)) {
                    viewModel.loadDecisionsForEvent(item.id)
                } else {
                    viewModel.removeEventAnswer(item.id)
                }
            }

            btnInfo.setOnClickListener {
                Snackbar.make(it, "Feature will be able in the future", Snackbar.LENGTH_SHORT)
                    .show()
            }

            val userDecision = item.userDecision
            if (userDecision != null) {
                tvAnswer.visibility = View.VISIBLE
                tvAnswerTitle.visibility = View.VISIBLE
                tvAnswer.text = userDecision.decision
                btnAnswer.apply {
                    text = context.getString(R.string.remove_answer)
                }
            } else {
                tvAnswer.visibility = View.GONE
                tvAnswerTitle.visibility = View.GONE
                tvAnswer.text = ""
                btnAnswer.apply {
                    text = context.getString(R.string.answer)
                }
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
