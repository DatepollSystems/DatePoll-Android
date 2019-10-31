package com.bke.datepoll.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneNumberModel(
    @PrimaryKey val id: Long,
    val user_id: Long,
    val label: String,
    val number: String,
    val created_at: String,
    val updated_at: String
)