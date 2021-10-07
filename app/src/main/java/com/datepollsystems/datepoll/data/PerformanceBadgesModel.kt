package com.datepollsystems.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PerformanceBadgesModel(
    val id: Long,
    val performanceBadge_id: Long,
    val instrument_id: Long,
    val grade: String?,
    val note: String?,
    val date: String?,
    val performanceBadge_name: String,
    val instrument_name: String
){
    fun getPerformanceBadgesDbModel(userId: Long): PerformanceBadgesDbModel {
        return PerformanceBadgesDbModel(
            id,
            performanceBadge_id,
            instrument_id,
            grade,
            note,
            date,
            performanceBadge_name,
            instrument_name,
            userId
        )
    }
}
