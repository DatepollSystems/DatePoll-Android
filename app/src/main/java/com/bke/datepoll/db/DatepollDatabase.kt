package com.bke.datepoll.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bke.datepoll.db.model.EmailAddressDbModel
import com.bke.datepoll.data.model.PerformanceBadgesModel
import com.bke.datepoll.data.model.PhoneNumberDbModel
import com.bke.datepoll.db.model.UserDbModel

@Database(entities = [  UserDbModel::class,
                        PhoneNumberDbModel::class,
                        PerformanceBadgesModel::class,
                        EmailAddressDbModel::class], version = 1)
abstract class DatepollDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

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