package com.datepollsystems.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.datepollsystems.datepoll.data.PermissionDbModel

@Dao
interface PermissionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPermissions(permissions: List<PermissionDbModel>)

    @Query("select * from permissions where user_id = :id")
    fun getAllPermissionsByUserId(id: Long): LiveData<List<PermissionDbModel>>
}