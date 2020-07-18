package com.datepollsystems.datepoll.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "performance_badges",
    indices = [Index(value = arrayOf("user_id"))],
    foreignKeys = [ForeignKey(onDelete = CASCADE, entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))]
)
data class PerformanceBadgesDbModel(
    @PrimaryKey val id: Long,
    val performance_badge_id: Long,
    val instrument_id: Long,
    val grade: String?,
    val note: String?,
    val date: String?,
    val performance_badge_name: String,
    val instrument_name: String,
    @ColumnInfo(name = "user_id") val userId: Long
)