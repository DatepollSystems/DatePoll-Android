package com.datepollsystems.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datepollsystems.datepoll.database.model.event.EventDateDbModel
import com.datepollsystems.datepoll.database.model.event.EventDbModel
import com.datepollsystems.datepoll.database.model.event.EventDecisionDbModel
import com.datepollsystems.datepoll.database.model.event.VoteForEventResponseDto
import kotlinx.coroutines.selects.select

@Dao
interface EventDao {

    @Query("select * from events")
    fun loadAllEvents(): LiveData<List<EventDbModel>>

    @Query("select *  from events where id = :id")
    fun getEventById(id: Int): EventDbModel

    @Query("select * from event_decisions where event_id = :eventId order by id")
    fun loadDecisionsForEvent(eventId: Int): List<EventDecisionDbModel>

    @Query("select * from events where already_voted = 0")
    fun getFilteredEvents(): LiveData<List<EventDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEvent(event: EventDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDecision(decision: EventDecisionDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDecisions(decisions: List<EventDecisionDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDates(dates: List<EventDateDbModel>)

    @Update
    fun updateEvent(e: EventDbModel)

    fun addUserDecision(it: VoteForEventResponseDto) {
        val e = getEventById(it.userDecision.eventId)
        updateEvent(
            EventDbModel(
                id = e.id,
                name = e.name,
                description = e.description,
                alreadyVoted = e.alreadyVoted,
                startDate = e.startDate,
                endDate = e.endDate,
                insertedAt = e.insertedAt,
                userDecision = it.getDbModelData(),
                forEveryone = e.forEveryone
            )
        )
    }

    fun removeUserDecision(eventId: Int){
        val e = getEventById(eventId)
        updateEvent(
            EventDbModel(
                id = e.id,
                name = e.name,
                description = e.description,
                alreadyVoted = e.alreadyVoted,
                startDate = e.startDate,
                endDate = e.endDate,
                insertedAt = e.insertedAt,
                userDecision = null,
                forEveryone = e.forEveryone
            )
        )
    }

}