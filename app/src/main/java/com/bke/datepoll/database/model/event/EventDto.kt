package com.bke.datepoll.database.model.event

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDto(
    val id: Int,
    val name: String,
    @field:Json(name = "start_date") val startDate: String,
    @field:Json(name = "end_date") val endDate: String,
    @field:Json(name = "for_everyone") val forEveryone: Int,
    val decisions: List<EventDecisionDto>,
    val dates: List<EventDateDto>,
    @field:Json(name = "already_voted") val alreadyVoted: Boolean,
    @field:Json(name = "user_decision") val userDecision: UserDecisionDto?
)

@JsonClass(generateAdapter = true)
data class UserDecisionDto(
    val id: Int,
    val decision: String,
    @field:Json(name = "shown_in_calendar") val shownInCalendar: Int,
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "created_at") val createdAt: String,
    @field:Json(name = "updated_at") val updatedAt: String,
    val color: String,
    @field:Json(name = "additional_information") val additionalInfo: String
)

@JsonClass(generateAdapter = true)
data class EventDateDto(
    val id: Int,
    val date: String,
    val x: Double,
    val y: Double,
    val description: String
)

@JsonClass(generateAdapter = true)
data class EventDecisionDto(
    val id: Int,
    val decision: String,
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "shown_in_calendar") val shownInCalendar: Int
)

