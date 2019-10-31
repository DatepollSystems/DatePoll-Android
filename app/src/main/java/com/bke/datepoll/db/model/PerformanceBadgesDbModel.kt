package com.bke.datepoll.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))])
data class PerformanceBadgesDbModel(
    @PrimaryKey val id: Long,
    val performance_badge_id: Long,
    val instrument_id: Long,
    val grade: String,
    val note: String?,
    val date: String,
    val performance_badge_name: String,
    val instrument_name: String,
    @ColumnInfo(name = "user_id") val userId: Long
)