package com.bke.datepoll.db.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "email_addresses",
    indices = [Index(value = arrayOf("user_id"))],
    foreignKeys = [ForeignKey(onDelete = CASCADE, entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))]
)
data class EmailAddressDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val email: String,

    @ColumnInfo(name = "user_id")
    val userId: Long
)