package com.bke.datepoll.data.model

import com.squareup.moshi.Json

data class Event(
    val id: Int,
    val name: String?,
    val description: String?,
    @field:Json(name = "start_date") val startDate: String?,
    @field:Json(name = "end_date") val endDate: String?,
    @field:Json(name = "for_everyone") val forEveryone: Int?,
    val location: String?,
    val decisions: List<Decision>,
    @field:Json(name = "already_voted") val alreadyVoted: Boolean
    )

data class Decision(
    val id: Int,
    val decision: String?,
    @field:Json(name = "event_id") val eventId: Int?,
    @field:Json(name = "show_in_calendar") val showInCalendar: Int?
    )

data class Booking(
    @field:Json(name = "movie_id") val movieId: Int,
    @field:Json(name = "movie_name") val movieName: String,
    @field:Json(name = "movie_date") val movieDate: String?,
    val amount: Int?,
    @field:Json(name = "worker_id") val workerId: Int?,
    @field:Json(name = "worker_name") val workerName: String?,
    @field:Json(name = "emergency_worker_id") val emergencyWorkerId: Int?,
    @field:Json(name = "emergency_worker_name") val emergencyWorkerName: String?
)

data class Birthday(
    val name: String?,
    val date: String?
)

data class HomeScreen(
    val msg: String?,
    val events: List<Event>,
    val bookings: List<Booking>,
    val birthdays: List<Birthday>
)
