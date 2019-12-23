package com.bke.datepoll.data.model

import com.bke.datepoll.db.model.PerformanceBadgesDbModel


data class PerformanceBadgesModel(
    val id: Long,
    val performance_badge_id: Long,
    val instrument_id: Long,
    val grade: String?,
    val note: String?,
    val date: String?,
    val performance_badge_name: String,
    val instrument_name: String
){
    fun getPerformanceBadgesDbModel(userId: Long): PerformanceBadgesDbModel {
        return PerformanceBadgesDbModel(
            id,
            performance_badge_id,
            instrument_id,
            grade,
            note,
            date,
            performance_badge_name,
            instrument_name,
            userId
        )
    }
}
