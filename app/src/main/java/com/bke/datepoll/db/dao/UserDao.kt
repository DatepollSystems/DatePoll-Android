package com.bke.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bke.datepoll.db.model.UserDbModel

@Dao
interface UserDao {
    //@Query("select * from users")
    //fun getAllUsers(): LiveData<List<UserDbModel>>

    @Query("select count(users.id) from users")
    fun getCount(): Long

    @Query("select * from users where id = :id")
    fun getUserById(id: Long) : LiveData<UserDbModel>

    @Query("select * from users LIMIT 1")
    fun getUser(): LiveData<UserDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserDbModel): Long

    @Update
    fun updateUser(user: UserDbModel)

    @Delete
    fun deleteUser(user: UserDbModel)
}