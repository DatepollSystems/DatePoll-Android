package com.datepollsystems.datepoll.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.datepollsystems.datepoll.components.main.event.model.EventDateDbModel
import com.datepollsystems.datepoll.components.main.event.model.EventDbModel
import com.datepollsystems.datepoll.components.main.event.model.EventDecisionDbModel
import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.db.dao.*

@Database(
    entities = [
        ApiModel::class,
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
    ], version = 17
)
@TypeConverters(Converters::class)
abstract class DatepollDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun phoneDao(): PhoneNumberDao
    abstract fun emailDao(): EmailDao
    abstract fun performanceBadgesDao(): PerformanceBadgesDao
    abstract fun permissionDao(): PermissionsDao
    abstract fun eventDao(): EventDao
    abstract fun cinemaDao(): CinemaDao
    abstract fun birthdayDao(): BirthdayDao
    abstract fun apiDao(): ApiDao

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