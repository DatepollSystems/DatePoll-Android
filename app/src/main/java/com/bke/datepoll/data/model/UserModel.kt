package com.bke.datepoll.data.model

import android.annotation.SuppressLint
import com.bke.datepoll.db.model.PhoneNumberDbModel
import com.bke.datepoll.db.model.UserDbModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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