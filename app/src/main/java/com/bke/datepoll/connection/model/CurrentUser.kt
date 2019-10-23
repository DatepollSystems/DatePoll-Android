package com.bke.datepoll.connection.model

data class CurrentUserResponseModel(
    val msg: String,
    val user : UserModel
)

data class UserModel(
    val id: Long,
    val title: String,
    val firstname: String,
    val surname: String,
    val username: String,
    val birthday: String,
    val join_date: String,
    val streetname: String,
    val zipcode: Int,
    val location: String,
    val activated: Int,
    val activity: String,
    val force_password_change: Int,
    val phone_numbers: List<String>,
    val email_addresses: List<String>,
    val permissions: List<String>,
    val performance_badges: List<String>

)