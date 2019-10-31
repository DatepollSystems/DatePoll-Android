package com.bke.datepoll.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bke.datepoll.db.model.UserDbModel

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAllUsers(): LiveData<List<UserDbModel>>

    @Query("select * from users where id = :id")
    fun getUserById(id: Long) : LiveData<UserDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserDbModel): Long

    @Update
    fun updateUser(user: UserDbModel)

    @Delete
    fun deleteUser(user: UserDbModel)
}