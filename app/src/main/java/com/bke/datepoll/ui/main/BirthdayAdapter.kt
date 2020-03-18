package com.bke.datepoll.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.data.Birthday
import java.util.*

class BirthdayAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var data = LinkedList<Birthday>()
        set(value) {
            value.addFirst(Birthday(null, null))
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder.itemViewType > 1){
            val item = data[position]
            val viewHolder = holder as BirthdayViewHolder
            viewHolder.tvName.text = item.name
            viewHolder.tvDate.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            1 -> BirthdayHeadlineViewHolder(layoutInflater.inflate(R.layout.birthday_headline_item, parent, false))
            else -> BirthdayViewHolder(layoutInflater.inflate(R.layout.birthady_view_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)
            1
        else
            2
    }
}

class BirthdayViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvBirthdayName)
    val tvDate: TextView = view.findViewById(R.id.tvBirthdayDate)
}

class BirthdayHeadlineViewHolder(val view: View): RecyclerView.ViewHolder(view)