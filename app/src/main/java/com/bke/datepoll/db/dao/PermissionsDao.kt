package com.bke.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bke.datepoll.db.model.PermissionDbModel

@Dao
interface PermissionsDao {

    @Insert
    fun addPermissions(permissions: List<PermissionDbModel>)

    @Query("select * from permissions where user_id = :id")
    fun getAllPermissionsByUserId(id: Long): LiveData<List<PermissionDbModel>>
}