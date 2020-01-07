package com.bke.datepoll.data.model

import com.squareup.moshi.Json

data class Event(
    val id: Int,
    val name: String?,
    val description: String?,
    @Json(name = "start_date") val startDate: String?,
    @Json(name = "end_date") val endDate: String?,
    @Json(name = "for_everyone") val forEveryone: Int?,
    val location: String?,
    val decisions: List<Decision>,
    @Json(name = "already_voted") val alreadyVoted: Boolean
    )

data class Decision(
    val id: Int,
    val decision: String?,
    @Json(name = "event_id") val eventId: Int?,
    @Json(name = "show_in_calendar") val showInCalendar: Int?
    )

data class Booking(
    @Json(name = "movie_id") val movieId: Int,
    @Json(name = "movie_date") val movieDate: String?,
    val amount: Int?,
    @Json(name = "worker_id") val workerId: Int?,
    @Json(name = "worker_name") val workerName: String?,
    @Json(name = "emergency_worker_id") val emergencyWorkerId: Int?,
    @Json(name = "emergency_worker_name") val emergencyWorkerName: String?
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
