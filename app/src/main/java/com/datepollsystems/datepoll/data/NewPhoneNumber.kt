package com.datepollsystems.datepoll.data

import com.datepollsystems.datepoll.database.model.PhoneNumberDbModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewPhoneNumberRequest(
    val label: String,
    val number: String
)

@JsonClass(generateAdapter = true)
data class NewPhoneNumberResponse(
    val msg: String,
    @field:Json(name = "phone_number") val phoneNumber: PhoneNumberDbModel
)