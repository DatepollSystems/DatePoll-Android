package com.datepollsystems.datepoll.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "permissions",
    indices = [Index(value = arrayOf("user_id"))],
    foreignKeys = [ForeignKey(onDelete = CASCADE, entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))]
)
data class PermissionDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val permission: String,
    @ColumnInfo(name = "user_id") val userId: Long
)