package com.bke.datepoll.database.model.event

import androidx.room.*

@Entity(tableName = "events")
data class EventDbModel(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "for_everyone") val forEveryone: Int,
    @ColumnInfo(name = "already_voted") val alreadyVoted: Boolean,
    @ColumnInfo(name = "inserted_at") val insertedAt: Long,
    @Embedded val userDecision: UserDecisionDbModel?
)

data class UserDecisionDbModel(
    val udId: Int,
    val decision: String,
    @ColumnInfo(name = "shown_in_calendar") val showInCalendar: Int,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
    val color: String,
    @ColumnInfo(name = "additional_information") val additionalInfo: String?,
    @ColumnInfo(name = "event_id") val eventId: Int
)

@Entity(tableName = "event_dates")
data class EventDateDbModel(
    @PrimaryKey val id: Int,
    val date: String,
    val x: Double,
    val y: Double,
    val description: String,
    @ColumnInfo(name = "event_id") val eventId: Int
)

@Entity(tableName = "event_decisions")
data class EventDecisionDbModel(
    @PrimaryKey val id: Int,
    val decision: String,
    @ColumnInfo(name = "event_id") val eventId: Int,
    @ColumnInfo(name = "shown_in_calendar") val shownInCalendar: Int
)

data class EventWithDecisions(
    @Embedded val event: EventDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "event_id"
    )
    val decisions: List<EventDecisionDbModel>
)

data class EventsDbDataHolder(
    val event: EventDbModel,
    val dates: List<EventDateDbModel>?,
    val decisions: List<EventDecisionDbModel>?
)