package com.bke.datepoll.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bke.datepoll.data.requests.UpdateUserRequest
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "users")
data class UserDbModel(

    @PrimaryKey
    val id: Long,

    val title: String?,

    val firstname: String?,

    val surname: String?,

    val username: String?,

    val birthday: Long?,

    val join_date: String?,

    val streetname: String?,

    val streetnumber: String?,

    val zipcode: Int?,

    val location: String?,

    val activated: Int?,

    val activity: String?,

    val force_password_change: Int?,

    val savedAt: Long
){
    fun getUpdateUserPart(): UpdateUserRequest{

        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date(birthday!!))

        return UpdateUserRequest(
                title!!,
                firstname!!,
                surname!!,
                streetname!!,
                streetnumber!!.toString(),
                zipcode!!,
                location!!,
                date
        )
    }
}