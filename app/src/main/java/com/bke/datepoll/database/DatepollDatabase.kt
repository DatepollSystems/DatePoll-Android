package com.bke.datepoll.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bke.datepoll.database.dao.*
import com.bke.datepoll.database.model.*
import com.bke.datepoll.database.model.event.EventDateDbModel
import com.bke.datepoll.database.model.event.EventDbModel
import com.bke.datepoll.database.model.event.EventDecisionDbModel
import com.bke.datepoll.database.model.event.UserDecisionDbModel

@Database(
    entities = [
        UserDbModel::class,
        PhoneNumberDbModel::class,
        PerformanceBadgesDbModel::class,
        EmailAddressDbModel::class,
        PermissionDbModel::class,
        EventDbModel::class,
        EventDecisionDbModel::class,
        EventDateDbModel::class
    ], version = 1
)
abstract class DatepollDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun phoneDao(): PhoneNumberDao
    abstract fun emailDao(): EmailDao
    abstract fun performanceBadgesDao(): PerformanceBadgesDao
    abstract fun permissionDao(): PermissionsDao
    abstract fun eventDao(): EventDao

    companion object {

        @Volatile
        private var INSTANCE: DatepollDatabase? = null

        fun getDatabase(context: Context): DatepollDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatepollDatabase::class.java,
                    "datepoll_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }



}