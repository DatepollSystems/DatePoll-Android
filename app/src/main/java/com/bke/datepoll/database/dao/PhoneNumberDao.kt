package com.bke.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bke.datepoll.database.model.PhoneNumberDbModel

@Dao
interface PhoneNumberDao {

    @Query("select * from phone_numbers")
    fun getPhoneNumbers() : LiveData<List<PhoneNumberDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSetOfPhoneNumbers(list: List<PhoneNumberDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePhoneNumer(phoneNumberDbModel: PhoneNumberDbModel)
}