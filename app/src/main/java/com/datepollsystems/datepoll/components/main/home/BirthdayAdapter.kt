package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.BirthdayDbModel
import com.datepollsystems.datepoll.databinding.HomeBirthdayItemBinding

class BirthdayAdapter : RecyclerView.Adapter<BirthdayAdapter.ViewHolder>() {

    var data = listOf<BirthdayDbModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ViewHolder private constructor(val binding: HomeBirthdayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BirthdayDbModel) {
            binding.tvName.text = item.name
            binding.tvDate.text = item.date
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