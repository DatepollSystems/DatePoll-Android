package com.datepollsystems.datepoll.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.datepollsystems.datepoll.data.Movie
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.database.dao.*
import com.datepollsystems.datepoll.database.model.*
import com.datepollsystems.datepoll.database.model.event.EventDateDbModel
import com.datepollsystems.datepoll.database.model.event.EventDbModel
import com.datepollsystems.datepoll.database.model.event.EventDecisionDbModel

@Database(
    entities = [
        UserDbModel::class,
        PhoneNumberDbModel::class,
        PerformanceBadgesDbModel::class,
        EmailAddressDbModel::class,
        PermissionDbModel::class,
        EventDbModel::class,
        EventDecisionDbModel::class,
        EventDateDbModel::class,
        MovieDbModel::class
    ], version = 3
)
abstract class DatepollDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun phoneDao(): PhoneNumberDao
    abstract fun emailDao(): EmailDao
    abstract fun performanceBadgesDao(): PerformanceBadgesDao
    abstract fun permissionDao(): PermissionsDao
    abstract fun eventDao(): EventDao
    abstract fun cinemaDao(): CinemaDao

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
                    "datepoll_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }


}