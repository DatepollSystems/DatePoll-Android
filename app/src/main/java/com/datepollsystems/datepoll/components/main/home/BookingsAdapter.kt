package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.databinding.HomeBookingItemBinding
import org.koin.core.KoinComponent

class BookingsAdapter(private val clickListener: BookingAdapterClickListener) :
    ListAdapter<MovieDbModel, BookingsAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder private constructor(val binding: HomeBookingItemBinding) :
        RecyclerView.ViewHolder(binding.root), KoinComponent {

        fun bind(item: MovieDbModel, clickListener: BookingAdapterClickListener) {
            binding.m = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeBookingItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class BookingAdapterClickListener(val clickListener: (movie: MovieDbModel) -> Unit) {
    fun onClick(movie: MovieDbModel) = clickListener(movie)
}