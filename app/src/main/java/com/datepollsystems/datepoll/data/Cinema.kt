package com.datepollsystems.datepoll.data

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*
import kotlin.collections.ArrayList


@JsonClass(generateAdapter = true)
data class GetMovieResponse(
    val msg: String,
    val movies: List<Movie>
)

@Entity(tableName = "movies")
data class MovieDbModel(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "trailer_link")
    val trailerLink: String?,

    @ColumnInfo(name = "poster_link")
    val posterLink: String?,

    @ColumnInfo(name = "booked_tickets")
    val bookedTickets: Int,

    @ColumnInfo(name = "movie_year_id")
    val movieYearId: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String,

    @ColumnInfo(name = "worker_id")
    val workerId: Int?,

    @ColumnInfo(name = "worker_name")
    val workerName: String?,

    @ColumnInfo(name = "emergency_worker_id")
    val emergencyWorkerId: Int?,

    @ColumnInfo(name = "emergency_worker_name")
    val emergencyWorkerName: String?,

    @ColumnInfo(name = "booked_tickets_for_yourself")
    val bookedTicketsForYourself: Int,

    @Embedded
    val viewMovie: ViewMovie,

    @ColumnInfo(name = "inserted_at")
    val inserted: Long = 0
)

@JsonClass(generateAdapter = true)
data class Movie(
    val id: Long,
    val name: String,
    val date: String,

    @field:Json(name = "trailer_link")
    val trailerLink: String?,

    @field:Json(name = "poster_link")
    val posterLink: String?,

    @field:Json(name = "booked_tickets")
    val bookedTickets: Int,

    @field:Json(name = "movie_year_id")
    val movieYearId: Int,

    @field:Json(name = "created_at")
    val createdAt: String,

    @field:Json(name = "updated_at")
    val updatedAt: String,

    @field:Json(name = "worker_id")
    val workerId: Int?,

    @field:Json(name = "worker_name")
    val workerName: String?,

    @field:Json(name = "emergency_worker_id")
    val emergencyWorkerId: Int?,

    @field:Json(name = "emergency_worker_name")
    val emergencyWorkerName: String?,

    @field:Json(name = "booked_tickets_for_yourself")
    val bookedTicketsForYourself: Int,

    @field:Json(name = "view_movie")
    val viewMovie: ViewMovie
)

@JsonClass(generateAdapter = true)
data class ViewMovie(
    val href: String,
    val method: String
)

fun List<Movie>.toDBModelList(): List<MovieDbModel> {
    val dbList = ArrayList<MovieDbModel>()
    this.forEach {
        dbList.add(it.toDbModel())
    }

    return dbList
}

fun Movie.toDbModel(): MovieDbModel {
    return MovieDbModel(
        id = this.id,
        name = this.name,
        date = this.date,
        trailerLink = this.trailerLink,
        posterLink = this.posterLink,
        bookedTickets = this.bookedTickets,
        movieYearId = this.movieYearId,
        bookedTicketsForYourself = this.bookedTicketsForYourself,
        createdAt = this.createdAt,
        emergencyWorkerId = this.emergencyWorkerId,
        emergencyWorkerName = this.emergencyWorkerName,
        inserted = Date().time,
        updatedAt = this.updatedAt,
        viewMovie = this.viewMovie,
        workerId = this.workerId,
        workerName = this.workerName
    )
}
