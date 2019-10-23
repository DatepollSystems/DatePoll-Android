package com.bke.datepoll.data.model

data class PhoneNumberModel(
    val id: Long,
    val user_id: Long,
    val label: String,
    val number: String,
    val created_at: String,
    val updated_at: String
)