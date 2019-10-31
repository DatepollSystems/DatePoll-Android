package com.bke.datepoll.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDbModel(

    @PrimaryKey
    val id: Long,

    val title: String?,

    val firstname: String?,

    val surname: String?,

    val username: String?,

    val birthday: String?,

    val join_date: String?,

    val streetname: String?,

    val zipcode: Int?,

    val location: String?,

    val activated: Int?,

    val activity: String?,

    val force_password_change: Int?
)