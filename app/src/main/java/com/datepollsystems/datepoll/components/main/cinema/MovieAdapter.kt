package com.datepollsystems.datepoll.components.main.cinema

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.data.MovieDbModel
import com.google.android.material.button.MaterialButton
import java.text.DateFormat
import java.text.SimpleDateFormat

class MovieAdapter(list: List<MovieDbModel>, val view: View, val vm: CinemaViewModel) : RecyclerView.Adapter<MovieViewHolder>() {

    var data: List<MovieDbModel> = list
        set(value) {
            val sorted = value.sortedBy { movie -> movie.date }
            field = sorted
            notifyDataSetChanged()
        }

    @SuppressLint("SimpleDateFormat")
    var enDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    var deDateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return MovieViewHolder(
            layoutInflater.inflate(
                R.layout.movie_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = data[position]

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.setStyle(CircularProgressDrawable.LARGE)
        circularProgressDrawable.start()


        Glide.with(holder.itemView.context)
            .load(item.posterLink)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(holder.ivMovie)

        val oldDate = enDateFormat.parse(item.date)
        val newDate = deDateFormat.format(oldDate!!)

        holder.tvMovieTitle.text = item.name
        holder.tvDescriptionShort.text = newDate
        holder.btnMore.setOnClickListener {
            vm.detailMovie.postValue(item)
            Navigation.findNavController(it).navigate(R.id.action_nav_cinema_to_movieDetailFragment)
        }
    }
}

class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val ivMovie: ImageView = view.findViewById(R.id.ivMovie)
    val tvMovieTitle: TextView = view.findViewById(R.id.tvMovieTitle)
    val tvDescriptionShort: TextView = view.findViewById(R.id.tvDescriptionShort)
    val btnMore: MaterialButton = view.findViewById(R.id.btnMovieMore)
}