package com.bke.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bke.datepoll.database.model.event.EventDateDbModel
import com.bke.datepoll.database.model.event.EventDbModel
import com.bke.datepoll.database.model.event.EventDecisionDbModel
import com.bke.datepoll.database.model.event.EventWithDecisions
import com.google.android.material.circularreveal.CircularRevealHelper

@Dao
interface EventDao {

    @Query("select * from events")
    fun loadAllEvents(): LiveData<List<EventDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEvent(event: EventDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDecision(decision: EventDecisionDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDecisions(decisions: List<EventDecisionDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDates(dates: List<EventDateDbModel>)
}