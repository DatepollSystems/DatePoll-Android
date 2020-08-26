package com.datepollsystems.datepoll.components.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.BirthdayDto
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class OldBithdayAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var data = LinkedList<BirthdayDto>()
        set(value) {
            value.addFirst(null)
            field = value
            notifyDataSetChanged()
        }

    @SuppressLint("SimpleDateFormat")
    var formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if(holder.itemViewType == 2){
            val item = data[position]
            val viewHolder = holder as OldBirthdayViewHolder
            viewHolder.tvName.text = item.name

            val date: Date? = formatter.parse(item.date)
            viewHolder.tvDate.text = dateFormat.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            1 -> BirthdayHeadlineViewHolder(
                layoutInflater.inflate(R.layout.birthday_headline_item, parent, false)
            )
            2 -> OldBirthdayViewHolder(
                layoutInflater.inflate(R.layout.birthady_view_item, parent, false)
            )
            else -> BirthdayHeadlineViewHolder(
                layoutInflater.inflate(R.layout.item_no_birthdays, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(data.size == 1)
            3
        else if(position == 0)
            1
        else
            2
    }
}

class OldBirthdayViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvBirthdayName)
    val tvDate: TextView = view.findViewById(R.id.tvBirthdayDate)
}

class BirthdayHeadlineViewHolder(val view: View): RecyclerView.ViewHolder(view)