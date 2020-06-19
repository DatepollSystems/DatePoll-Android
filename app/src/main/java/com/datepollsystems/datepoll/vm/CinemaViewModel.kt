package com.datepollsystems.datepoll.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.repos.CinemaRepository
import com.datepollsystems.datepoll.repos.ENetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class CinemaViewModel: ViewModel(), KoinComponent {

    val repository: CinemaRepository by inject()

    val movies = repository.movies
    val detailMovie = MutableLiveData<MovieDbModel>()

    val loadMoviesState = MutableLiveData<ENetworkState>()

    fun loadMovies(force: Boolean = false){
        viewModelScope.launch(Dispatchers.Default){
            repository.loadNotShownMovies(force, loadMoviesState)
        }
    }
}