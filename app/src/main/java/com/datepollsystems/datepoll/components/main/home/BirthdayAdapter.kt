package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.BirthdayDbModel
import com.datepollsystems.datepoll.databinding.HomeBirthdayItemBinding

class BirthdayAdapter : ListAdapter<BirthdayDbModel, BirthdayAdapter.ViewHolder>(BirthdayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: HomeBirthdayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BirthdayDbModel) {
            binding.b = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeBirthdayItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}