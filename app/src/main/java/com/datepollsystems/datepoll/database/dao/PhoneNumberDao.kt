package com.datepollsystems.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.datepollsystems.datepoll.database.model.PhoneNumberDbModel

@Dao
interface PhoneNumberDao {

    @Query("select * from phone_numbers")
    fun getPhoneNumbers() : LiveData<List<PhoneNumberDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSetOfPhoneNumbers(list: List<PhoneNumberDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePhoneNumber(phoneNumberDbModel: PhoneNumberDbModel)

    @Query("DELETE from phone_numbers where id = :id")
    fun deletePhoneNumber(id: Long)
}