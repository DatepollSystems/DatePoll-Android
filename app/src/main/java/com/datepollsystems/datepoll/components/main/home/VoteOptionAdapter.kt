package com.datepollsystems.datepoll.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.database.model.event.EventDecisionDbModel

class VoteOptionAdapter(val sheet: VoteBottomSheetDialog, val response: MutableLiveData<EventDecisionDbModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<EventDecisionDbModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DecisionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.decision_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as DecisionViewHolder
        val i = data[position]

        h.text.text = i.decision
        h.line.setOnClickListener {
            response.postValue(i)
            sheet.dismiss()
        }
    }
}

class DecisionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.textView)
    val line: LinearLayout = view.findViewById(R.id.line)
}