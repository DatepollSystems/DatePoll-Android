package com.datepollsystems.datepoll.components.main.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.data.MovieOrder
import com.datepollsystems.datepoll.databinding.HomeMovieWorkerBottomSheetBinding
import com.datepollsystems.datepoll.repos.CinemaRepository
import com.datepollsystems.datepoll.utils.formatDateEnToLocal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class CinemaWorkerInfoBottomSheet(private val movieId: Long) : BottomSheetDialogFragment(),
    KoinComponent {

    private val cinemaRepository: CinemaRepository by inject()
    private lateinit var dataHolder: BottomSheetDataHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = HomeMovieWorkerBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val orders: LiveData<List<MovieOrder>> = liveData { emitSource(cinemaRepository.loadOrdersForMovie(movieId).asLiveData()) }
        val movie: LiveData<MovieDbModel> = liveData { emitSource(cinemaRepository.getMovieById(movieId)) }

        dataHolder = BottomSheetDataHolder(movie, orders)
        binding.d = dataHolder

        val adapter = MovieWorkerBottomSheetAdapter()
        orders.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                binding.tvReservation.text = it.size.toString()
            }
        })

        adapter.submitList(dataHolder.orders.value)
        binding.orderList.adapter = adapter

        movie.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.apply {
                    tvMovieWorkerHeadline.text = getString(
                        R.string.info_for_date,
                        formatDateEnToLocal(it.date)
                    )
                }
            }
        })

        binding.apply {
            tvMovieWorkerHeadline.text = getString(
                R.string.info_for_date,
                formatDateEnToLocal(movie.value?.date ?: "")
            )
        }

        return binding.root
    }

}

data class BottomSheetDataHolder(
    val movie: LiveData<MovieDbModel>,
    val orders: LiveData<List<MovieOrder>>
){
    val showIfNoEntries = Transformations.map(orders) {
        if(it.isEmpty())
            View.VISIBLE
        else
            View.INVISIBLE
    }

    val showIfEntries = Transformations.map(orders) {
        if(it.isEmpty())
            View.INVISIBLE
        else
            View.VISIBLE
    }
}