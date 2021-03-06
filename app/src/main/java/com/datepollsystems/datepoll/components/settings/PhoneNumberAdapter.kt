package com.datepollsystems.datepoll.components.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.PhoneNumberDbModel
import org.koin.core.component.KoinComponent
import java.util.*

class PhoneNumberAdapter(val vm: SettingsViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    KoinComponent {
    val tag = "PhoneNumberAdapter"

    var data = LinkedList<PhoneNumberDbModel>()
        set(value) {
            value.addFirst(
                PhoneNumberDbModel(
                    1,
                    1,
                    "",
                    "",
                    "",
                    ""
                )
            )
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder.itemViewType > 1){
            val item = data[position]
            val viewHolder = holder as PhoneNumberViewHolder
            viewHolder.tvNumber.text = item.number
            viewHolder.tvLabel.text = item.label
            viewHolder.btnDelete.setOnClickListener {
                vm.removePhoneNumber(data[position].id.toInt())
                Log.i(tag, "Deleted item")
            }

            if(position + 1 == itemCount){
                viewHolder.divider.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            1 -> PhoneNumberHeadlineViewHolder(layoutInflater.inflate(R.layout.phone_number_headline, parent, false))
            else -> PhoneNumberViewHolder(layoutInflater.inflate(R.layout.phone_number_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)
            1
        else
            2
    }
}

class PhoneNumberViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val tvLabel: TextView = view.findViewById(R.id.tvLabel)
    val tvNumber: TextView = view.findViewById(R.id.tvNumber)
    val btnDelete: Button = view.findViewById(R.id.btnDelete)
    val divider: View = view.findViewById(R.id.divider)
}

class PhoneNumberHeadlineViewHolder(val view: View): RecyclerView.ViewHolder(view)