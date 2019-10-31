package com.bke.datepoll.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "email_addresses",
    foreignKeys = [ForeignKey(entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))]
)
data class EmailAddressDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val email: String,

    @ColumnInfo(name = "user_id")
    val userId: Long
)