package com.bke.datepoll.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.data.Decision

class VoteOptionAdapter(val sheet: VoteBottomSheetDialog, val liveData: MutableLiveData<Decision>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<Decision>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DecisionViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.decision_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as DecisionViewHolder
        val i = data[position]

        h.text.text = i.decision
        h.line.setOnClickListener {
            liveData.postValue(i)
            Thread.sleep(200)
            sheet.dismiss()
        }
    }
}

class DecisionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.textView)
    val line: LinearLayout = view.findViewById(R.id.line)
}