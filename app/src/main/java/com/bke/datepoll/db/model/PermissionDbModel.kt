package com.bke.datepoll.db.model

import androidx.room.*

@Entity(
    tableName = "permissions",
    indices = [Index(value = arrayOf("user_id"))],
    foreignKeys = [ForeignKey(entity = UserDbModel::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"))]
)
data class PermissionDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val permission: String,
    @ColumnInfo(name = "user_id") val userId: Long
)