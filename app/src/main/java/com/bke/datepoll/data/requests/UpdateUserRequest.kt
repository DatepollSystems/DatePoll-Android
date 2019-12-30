package com.bke.datepoll.data.requests

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
