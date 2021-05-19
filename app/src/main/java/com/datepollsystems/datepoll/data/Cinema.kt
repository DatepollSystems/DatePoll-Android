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
    var name: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "trailer_link")
    val trailerLink: String?,

    @ColumnInfo(name = "poster_link")
    var posterLink: String?,

    @ColumnInfo(name = "booked_tickets")
    var bookedTickets: Int,

    @ColumnInfo(name = "created_at")
    var createdAt: String,

    @ColumnInfo(name = "updated_at")
    var updatedAt: String,

    @ColumnInfo(name = "worker_id")
    var workerId: Int?,

    @ColumnInfo(name = "worker_name")
    var workerName: String?,

    @ColumnInfo(name = "emergency_worker_id")
    var emergencyWorkerId: Int?,

    @ColumnInfo(name = "emergency_worker_name")
    var emergencyWorkerName: String?,

    @ColumnInfo(name = "booked_tickets_for_yourself")
    var bookedTicketsForYourself: Int,

    @ColumnInfo(name = "inserted_at")
    var inserted: Long = 0
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
        bookedTicketsForYourself = this.bookedTicketsForYourself,
        createdAt = this.createdAt,
        emergencyWorkerId = this.emergencyWorkerId,
        emergencyWorkerName = this.emergencyWorkerName,
        inserted = Date().time,
        updatedAt = this.updatedAt,
        workerId = this.workerId,
        workerName = this.workerName
    )
}

@JsonClass(generateAdapter = true)
data class BookTicketsRequestModel(
    @field:Json(name = "movie_id") val movieId: Int,
    @field:Json(name = "ticket_amount") val ticketAmount: Int
)

@JsonClass(generateAdapter = true)
data class BookTicketsResponseModel(
    val msg: String,
    @field:Json(name = "movie_booking") val movieBooking: MovieBooking
)

@JsonClass(generateAdapter = true)
data class MovieBooking(

    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "user_id")
    val userId: Long,

    @field:Json(name = "movie_id")
    val movieId: Long,

    @field:Json(name = "amount")
    val amount: Int,

    @field:Json(name = "created_at")
    val createdAt: String,

    @field:Json(name = "updated_at")
    val updatedAt: String
)


@Entity(tableName = "movie_orders")
data class MovieOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "movie_id")
    val movieId: Long
)

data class MovieOrderTupl(
    @field:Json(name = "movie_name")
    val movieName: String,

    @field:Json(name = "date")
    val date: String,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "booked_tickets")
    val bookedTickets: Int
)

@JsonClass(generateAdapter = true)
data class MovieWorkerDetailsDto(
    @field:Json(name = "movie_id")
    val movieId: Long,

    @field:Json(name = "movie_name")
    val movieName: String,

    @field:Json(name = "date")
    val date: String,

    @field:Json(name = "orders")
    val orders: List<MovieOrdersDto>
) {
    fun toDbModelData(): List<MovieOrder> {
        val res = ArrayList<MovieOrder>()
        orders.forEach {
            res.add(it.toDbModel(movieId))
        }
        return res
    }
}

@JsonClass(generateAdapter = true)
data class MovieOrdersDto(
    @field:Json(name = "user_name")
    val userName: String,

    @field:Json(name = "user_id")
    val userId: Long,

    @field:Json(name = "amount")
    val amount: Int
) {
    fun toDbModel(movieId: Long): MovieOrder {
        return MovieOrder(
            userName = userName,
            userId = userId,
            amount = amount,
            movieId = movieId,
            id = 0L
        )
    }
}


@JsonClass(generateAdapter = true)
data class GetMovieWorkerDetailsResponse(
    val msg: String,
    val movies: List<MovieWorkerDetailsDto>
)





