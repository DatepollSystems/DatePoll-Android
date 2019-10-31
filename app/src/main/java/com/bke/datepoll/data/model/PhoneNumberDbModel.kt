package com.bke.datepoll.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bke.datepoll.db.model.UserDbModel

@Entity(tableName = "phone_numbers", foreignKeys = [ForeignKey(entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))])
data class PhoneNumberDbModel(
    @PrimaryKey val id: Long,
    val user_id: Long,
    val label: String,
    val number: String,
    val created_at: String,
    val updated_at: String
)