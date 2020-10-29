package com.datepollsystems.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datepollsystems.datepoll.data.UserDbModel

@Dao
interface UserDao {

    @Query("select count(users.id) from users")
    fun getCount(): Long

    @Query("select * from users where id = :id")
    fun getUserById(id: Long) : LiveData<UserDbModel>

    @Query("select * from users LIMIT 1")
    fun getUser(): LiveData<UserDbModel>

    @Query("select savedAt from users LIMIT 1")
    fun getSavedAt(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserDbModel): Long

    @Delete
    fun deleteUser(user: UserDbModel)
}