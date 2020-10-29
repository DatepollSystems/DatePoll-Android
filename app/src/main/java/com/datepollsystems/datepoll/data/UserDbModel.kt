package com.datepollsystems.datepoll.data

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.datepollsystems.datepoll.data.UpdateUserRequest
import com.datepollsystems.datepoll.utils.formatDateToLocal
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

    var birthday: Long?,

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
    fun getBirthdayString(): String{
        return formatDateToLocal(Date(birthday!!))
    }

    fun setBirthdayString(string: String){
    }
    @SuppressLint("SimpleDateFormat")
    fun getUpdateUserPart(): UpdateUserRequest {

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