package com.datepollsystems.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datepollsystems.datepoll.data.ApiModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiDao {

    @Query("select count(*) from api")
    fun countOfApi(): Int

    @Query("select * from api limit 1")
    fun getApi(): ApiModel

    @Query("select * from api limit 1")
    fun getApiFlow(): Flow<ApiModel>

    @Query("delete from api")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApi(api: ApiModel)
}