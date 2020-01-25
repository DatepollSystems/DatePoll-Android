package com.bke.datepoll.data.model

import com.bke.datepoll.database.model.PhoneNumberDbModel
import com.squareup.moshi.Json

data class NewPhoneNumberRequest(
    val label: String,
    val number: String
)

data class NewPhoneNumberResponse(
    val msg: String,
    @field:Json(name = "phone_number") val phoneNumber: PhoneNumberDbModel
)