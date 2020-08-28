package com.datepollsystems.datepoll.db.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.datepollsystems.datepoll.database.dao.*
import com.datepollsystems.datepoll.components.main.event.model.EventDateDbModel
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDbModel
import com.datepollsystems.datepoll.data.*

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
        MovieDbModel::class,
        BirthdayDbModel::class,
        MovieOrder::class
    ], version = 7
)
abstract class DatepollDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun phoneDao(): PhoneNumberDao
    abstract fun emailDao(): EmailDao
    abstract fun performanceBadgesDao(): PerformanceBadgesDao
    abstract fun permissionDao(): PermissionsDao
    abstract fun eventDao(): EventDao
    abstract fun cinemaDao(): CinemaDao
    abstract fun birthdayDao(): BirthdayDao

    companion object {

        @Volatile
        private var INSTANCE: DatepollDatabase? = null

        fun getDatabase(context: Context): DatepollDatabase {
            val tempInstance =
                INSTANCE
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