package com.datepollsystems.datepoll.components.main.event

import androidx.recyclerview.widget.DiffUtil
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel

class EventDiffCallback: DiffUtil.ItemCallback<EventDbModel>(){

    override fun areItemsTheSame(oldItem: EventDbModel, newItem: EventDbModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventDbModel, newItem: EventDbModel): Boolean {
        return oldItem == newItem
    }
}