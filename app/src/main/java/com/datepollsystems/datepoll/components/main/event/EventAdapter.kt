package com.datepollsystems.datepoll.components.main.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel
import com.datepollsystems.datepoll.databinding.EventItemBinding
import org.koin.core.KoinComponent

class EventAdapter(
    private val clickListener: EventDecisionClickListener,
    private val longClickListener: EventLongClickListener
) :
    ListAdapter<EventDbModel, EventAdapter.ViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), longClickListener, clickListener)
    }

    class ViewHolder private constructor(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root), KoinComponent {

        fun bind(
            item: EventDbModel,
            onLongClickListener: EventLongClickListener,
            onClickListener: EventDecisionClickListener
        ) {
            binding.e = item
            binding.clickListener = onClickListener
            binding.eventCard.setOnLongClickListener {
                onLongClickListener.clickListener.invoke(item)
                true
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EventItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class EventLongClickListener(val clickListener: (event: EventDbModel) -> Unit) {
    fun onClick(event: EventDbModel) = clickListener(event)
}

class EventDecisionClickListener(val clickListener: (event: EventDbModel) -> Unit) {
    fun onClick(event: EventDbModel) = clickListener(event)
}