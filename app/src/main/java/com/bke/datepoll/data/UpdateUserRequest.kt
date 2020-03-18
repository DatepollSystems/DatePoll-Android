package com.bke.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateUserRequest(
        val title: String,
        val firstname: String,
        val surname: String,
        val streetname: String,
        val streetnumber: String,
        val zipcode: Int,
        val location: String,
        val birthday: String
)
