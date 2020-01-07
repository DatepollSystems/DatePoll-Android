package com.bke.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bke.datepoll.database.model.PerformanceBadgesDbModel

@Dao
interface PerformanceBadgesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPerformanceBadges(badges: List<PerformanceBadgesDbModel>)

    @Query("select * from performance_badges where user_id = :id")
    fun getPerformanceBadgesByUserId(id: Long): LiveData<List<PerformanceBadgesDbModel>>

    @Query("delete from performance_badges")
    fun deleteAllEntries()
}