package com.bke.datepoll.data.model

data class PerformanceBadgesModel(
    val id: Long,
    val performance_badge_id: Long,
    val instrument_id: Long,
    val grade: String,
    val note: String?,
    val date: String,
    val performance_badge_name: String,
    val instrument_name: String
)
