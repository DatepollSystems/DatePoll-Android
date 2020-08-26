package com.datepollsystems.datepoll.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*

@Entity(tableName = "birthdays")
data class BirthdayDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var name: String,
    var date: String,
    @ColumnInfo(name = "inserted_at") var insertedAt: Long = Date().time
)

@JsonClass(generateAdapter = true)
data class BirthdayDto(
    val name: String,
    val date: String
) {
    fun toDbModel(): BirthdayDbModel {
        return BirthdayDbModel(
            id = 0,
            name = name,
            date = date
        )
    }
}
