package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.Event
import java.util.*
import kotlin.collections.ArrayList

class EventAdapter(val activity: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = LinkedList<Event>()
        set(value) {
            value.addFirst(
                Event(
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    ArrayList(),
                    false
                )
            )
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType > 1) {
            val item = data[position]
            val viewHolder = holder as EventViewHolder
            viewHolder.tvName.text = item.name
            viewHolder.tvDate.text = item.startDate

            viewHolder.btnAnswer.setOnClickListener {
                //val bottomSheetFragment = VoteBottomSheetDialog(item., MutableLiveData())
                //bottomSheetFragment.show(activity.supportFragmentManager, bottomSheetFragment.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            1 -> EventHeadlineViewHolder(
                layoutInflater.inflate(
                    R.layout.event_headline_item,
                    parent,
                    false
                )
            )
            else -> EventViewHolder(
                layoutInflater.inflate(
                    R.layout.event_view_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            1
        else
            2
    }
}

class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvEventName)
    val tvDate: TextView = view.findViewById(R.id.tvDate)
    val btnAnswer: Button = view.findViewById(R.id.btnAnswer)
}

class EventHeadlineViewHolder(val view: View) : RecyclerView.ViewHolder(view)