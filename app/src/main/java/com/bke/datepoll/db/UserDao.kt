package com.bke.datepoll.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bke.datepoll.data.model.UserModel

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAllUsers(): LiveData<List<UserModel>>

    @Query("select * from users where id = :id")
    fun getUserById(id: Long) : LiveData<UserModel>

    @Insert
    fun addUser(user: UserModel)

    @Update
    fun updateUser(user: UserModel)

    @Delete
    fun deleteUser(user: UserModel)
}