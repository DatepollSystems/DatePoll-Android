package com.bke.datepoll.data.model

import com.squareup.moshi.Json

data class VoteResponse(
    val msg: String,
    val eventUserVotedForDecision: EventUserVotedForDecision
)

data class EventUserVotedForDecision(
    val additionalInformation: String?,
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "decision_id") val decisionId: Int,
    @field:Json(name = "user_id") val userId: Int,
    @field:Json(name = "updated_at") val updatedAt: String,
    @field:Json(name = "created_at") val createdAt: String,
    val id: Int
)

data class VoteRequest(
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "decision_id") val decisionId: Int,
    @field:Json(name = "additional_information") val additionalInformation: String?
)