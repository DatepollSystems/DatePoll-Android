package com.bke.datepoll.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.data.model.Birthday

class BirthdayAdapter: RecyclerView.Adapter<BirthdayViewHolder>(){
    var data = listOf<Birthday>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) {
        val item = data[position]

        holder.tvName.text = item.name
        holder.tvDate.text = item.date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.birthady_view_item, parent, false)



        return BirthdayViewHolder(view)
    }
}

class BirthdayViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvBirthdayName)
    val tvDate: TextView = view.findViewById(R.id.tvBirthdayDate)
}
