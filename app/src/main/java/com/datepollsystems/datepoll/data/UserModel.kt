package com.datepollsystems.datepoll.data

import android.annotation.SuppressLint
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class UserModel(
    val id: Long,

    val title: String?,

    val firstname: String?,

    val surname: String?,

    val username: String?,

    val birthday: String?,

    val join_date: String?,

    val streetname: String?,

    val streetnumber: String?,

    val zipcode: Int?,

    val location: String?,

    val activated: Int?,

    val activity: String?,

    val force_password_change: Int?,


    val phone_numbers: List<PhoneNumberDbModel>,

    val email_addresses: List<String>,

    val permissions: List<String>,

    val performance_badges: List<PerformanceBadgesModel>
){
    @SuppressLint("SimpleDateFormat")
    fun getUserDbModelPart(): UserDbModel {

        val date = SimpleDateFormat("yyyy-MM-dd").parse(birthday!!)!!

        return UserDbModel(
            id,
            title,
            firstname,
            surname,
            username,
            date.time,
            join_date,
            streetname,
            streetnumber,
            zipcode,
            location,
            activated,
            activity,
            force_password_change,
            Date().time
        )
    }
}


/*
fun NetworkVideoContainer.asDatabaseModel(): Array<DatabaseVideo> {
    return videos.map {
        DatabaseVideo (
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail)
    }.toTypedArray()
}*/