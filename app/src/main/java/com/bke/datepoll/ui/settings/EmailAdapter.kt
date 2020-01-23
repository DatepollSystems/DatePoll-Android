package com.bke.datepoll.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bke.datepoll.R
import com.bke.datepoll.database.model.EmailAddressDbModel
import java.util.*

class EmailAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = LinkedList<EmailAddressDbModel>()
        set(value) {
            value.addFirst(EmailAddressDbModel("", 0))
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            1 -> EmailHeadlineViewHolder(
                layoutInflater.inflate(
                    R.layout.email_headline,
                    parent,
                    false
                )
            )
            else -> EmailViewHolder(
                layoutInflater.inflate(
                    R.layout.email_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType > 1) {
            val item = data[position]
            val viewHolder = holder as EmailViewHolder
            viewHolder.email.text = item.email

            viewHolder.action.setOnClickListener {
               // TODO
            }

            if(position + 1 == itemCount){
                viewHolder.divider.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            1
        else
            2
    }

}

class EmailViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val email: TextView = view.findViewById(R.id.tvEmail)
    val action: Button = view.findViewById(R.id.btnAction)
    val divider: View = view.findViewById(R.id.divider)
}

class EmailHeadlineViewHolder(view: View): RecyclerView.ViewHolder(view) {

}