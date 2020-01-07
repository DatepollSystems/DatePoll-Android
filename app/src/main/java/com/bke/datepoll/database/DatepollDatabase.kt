package com.bke.datepoll.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bke.datepoll.database.dao.*
import com.bke.datepoll.database.model.*

@Database(entities = [  UserDbModel::class,
                        PhoneNumberDbModel::class,
                        PerformanceBadgesDbModel::class,
                        EmailAddressDbModel::class,
                        PermissionDbModel::class], version = 2)
abstract class DatepollDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun phoneDao(): PhoneNumberDao
    abstract fun emailDao(): EmailDao
    abstract fun performanceBadgesDao(): PerformanceBadgesDao
    abstract fun permissionDao(): PermissionsDao

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
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}