package com.datepollsystems.datepoll.components.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datepollsystems.datepoll.data.MovieOrder
import com.datepollsystems.datepoll.databinding.HomeMovieWorkerOrderItemBinding
import org.koin.core.component.KoinComponent

class MovieWorkerBottomSheetAdapter :
    ListAdapter<MovieOrder, MovieWorkerBottomSheetAdapter.ViewHolder>(MovieOrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: HomeMovieWorkerOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root), KoinComponent {

        fun bind(item: MovieOrder) {
            binding.order = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeMovieWorkerOrderItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}