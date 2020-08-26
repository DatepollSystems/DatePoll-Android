package com.datepollsystems.datepoll.components.main.home

import androidx.recyclerview.widget.DiffUtil
import com.datepollsystems.datepoll.data.BirthdayDbModel
import com.datepollsystems.datepoll.data.MovieDbModel

class BirthdayDiffCallback: DiffUtil.ItemCallback<BirthdayDbModel>(){
    override fun areItemsTheSame(oldItem: BirthdayDbModel, newItem: BirthdayDbModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BirthdayDbModel, newItem: BirthdayDbModel): Boolean {
        return oldItem == newItem
    }
}

class BookingsDiffCallback: DiffUtil.ItemCallback<MovieDbModel>(){
    override fun areItemsTheSame(oldItem: MovieDbModel, newItem: MovieDbModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieDbModel, newItem: MovieDbModel): Boolean {
        return oldItem == newItem
    }
}