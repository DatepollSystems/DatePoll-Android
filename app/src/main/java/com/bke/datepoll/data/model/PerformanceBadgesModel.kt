package com.bke.datepoll.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PerformanceBadgesModel(
    @PrimaryKey val id: Long,
    val performance_badge_id: Long,
    val instrument_id: Long,
    val grade: String,
    val note: String?,
    val date: String,
    val performance_badge_name: String,
    val instrument_name: String
)
