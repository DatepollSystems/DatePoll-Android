package com.datepollsystems.datepoll.components.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.SessionModel
import java.util.*

class SessionsAdapter(val vm: SettingsViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val tag = "SessionAdapter"

    var data = LinkedList<SessionModel>()
        set(value) {
            value.addFirst(null)
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            1 -> SessionHeadlineViewHolder(layoutInflater.inflate(R.layout.session_headline_item, parent, false))
            else -> SessionViewHolder(layoutInflater.inflate(R.layout.session_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType > 1){
            val item = data[position]
            val viewHolder = holder as SessionViewHolder
            viewHolder.tvInformation.text = item.information
            viewHolder.tvLastUsage.text = item.lastUsed
            viewHolder.btnDelete.setOnClickListener {
                vm.deleteSession(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)
            1
        else
            2
    }
}

class SessionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvInformation: TextView = view.findViewById(R.id.tvInformation)
    val tvLastUsage: TextView = view.findViewById(R.id.tvLastUsed)
    val btnDelete: Button = view.findViewById(R.id.btnDelete)
}

class SessionHeadlineViewHolder(view: View): RecyclerView.ViewHolder(view)