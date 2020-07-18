package com.datepollsystems.datepoll.components.main.event.model

import com.datepollsystems.datepoll.utils.transformInDbModelList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class GetAllEventsResponseMsg(
    val msg: String,
    val events: List<EventDto>
)

@JsonClass(generateAdapter = true)
data class EventDto(
    val id: Int,
    val name: String,
    val description: String,
    @field:Json(name = "start_date") val startDate: String,
    @field:Json(name = "end_date") val endDate: String,
    @field:Json(name = "for_everyone") val forEveryone: Int,
    val decisions: List<EventDecisionDto>,
    val dates: List<EventDateDto>,
    @field:Json(name = "already_voted") val alreadyVoted: Boolean,
    @field:Json(name = "user_decision") val userDecision: UserDecisionDto?
) {
    fun getDbData(): EventsDbDataHolder {
        userDecision?.let {
            return EventsDbDataHolder(
                event = EventDbModel(
                    id = id,
                    name = name,
                    description = description,
                    startDate = startDate,
                    endDate = endDate,
                    forEveryone = forEveryone,
                    userDecision = UserDecisionDbModel(
                        udId = it.id,
                        decision = it.decision,
                        showInCalendar = it.showInCalendar,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        color = it.color,
                        additionalInfo = it.additionalInfo,
                        eventId = id
                    ),
                    insertedAt = Date().time,
                    alreadyVoted = alreadyVoted
                ),
                dates = dates.transformInDbModelList(id),
                decisions = decisions.transformInDbModelList()
            )
        }

        return EventsDbDataHolder(
            event = EventDbModel(
                id = id,
                name = name,
                description = description,
                startDate = startDate,
                endDate = endDate,
                forEveryone = forEveryone,
                userDecision = null,
                insertedAt = Date().time,
                alreadyVoted = alreadyVoted
            ),
            dates = dates.transformInDbModelList(id),
            decisions = decisions.transformInDbModelList()
        )
    }
}

@JsonClass(generateAdapter = true)
data class UserDecisionDto(
    val id: Int,
    val decision: String,
    @field:Json(name = "show_in_calendar") val showInCalendar: Int,
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "created_at") val createdAt: String,
    @field:Json(name = "updated_at") val updatedAt: String,
    val color: String,
    @field:Json(name = "additional_information") val additionalInfo: String?
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
    @field:Json(name = "show_in_calendar") val showInCalendar: Int
)

@JsonClass(generateAdapter = true)
data class VoteForEventRequestDto(
    @field:Json(name = "decision_id") val decisionId: Int,
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "additional_info")val additionalInfo: String
)

@JsonClass(generateAdapter = true)
data class VoteForEventResponseDto(
    val msg: String,
    @field:Json(name = "user_decision") val userDecision: UserDecisionDto
){
    fun getDbModelData(): UserDecisionDbModel{
        return UserDecisionDbModel(
            udId = userDecision.id,
            decision = userDecision.decision,
            eventId = userDecision.eventId,
            showInCalendar = userDecision.showInCalendar,
            createdAt = userDecision.createdAt,
            updatedAt = userDecision.updatedAt,
            color = userDecision.color,
            additionalInfo = userDecision.additionalInfo
        )
    }
}

/**
@JsonClass(generateAdapter = true)
data class EventVoteForDecision(
    val additionalInformation: String?,
    @field:Json(name = "event_id") val eventId: Int,
    @field:Json(name = "decision_id") val decisionId: Int,
    @field:Json(name = "user_id") val userId: Int,
    @field:Json(name = "created_at") val createdAt: String,
    @field:Json(name = "updated_at") val updatedAt: String,
    val id: Int
)**/
