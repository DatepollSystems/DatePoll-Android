package com.bke.datepoll.data.model

import com.bke.datepoll.db.model.PhoneNumberDbModel
import com.bke.datepoll.db.model.UserDbModel
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
    fun getUserDbModelPart(): UserDbModel {
        return UserDbModel(
            id,
            title,
            firstname,
            surname,
            username,
            birthday,
            join_date,
            streetname,
            zipcode,
            location,
            activated,
            activity,
            force_password_change,
            Date().time
        )
    }
}