package com.datepollsystems.datepoll.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(
    tableName = "phone_numbers",
    indices = [Index(value = arrayOf("user_id"))],
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = UserDbModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"))
    ])
data class PhoneNumberDbModel(
    @PrimaryKey val id: Long,
    val user_id: Long,
    val label: String,
    val number: String,
    val created_at: String,
    val updated_at: String
)