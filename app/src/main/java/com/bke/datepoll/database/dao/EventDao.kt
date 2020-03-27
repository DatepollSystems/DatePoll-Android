package com.bke.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.bke.datepoll.database.model.event.EventDbModel
import com.bke.datepoll.database.model.event.EventDecisionDbModel
import com.bke.datepoll.database.model.event.EventWithDecisions

@Dao
interface EventDao {

    @Transaction
    @Query("select * from events")
    fun loadAllEvents(): LiveData<List<EventWithDecisions>>

    @Insert
    fun addEvent(event: EventDbModel)

    @Insert
    fun addDecision(decision: EventDecisionDbModel)

    @Insert
    fun addDecisions(decisions: List<EventDecisionDbModel>)
}