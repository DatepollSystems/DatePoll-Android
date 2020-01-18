package com.bke.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bke.datepoll.database.model.EmailAddressDbModel

@Dao
interface EmailDao {

    @Query("select * from email_addresses where user_id = :id")
    fun getEmailsOfUser(id: Long): LiveData<List<EmailAddressDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEmails(emails: List<EmailAddressDbModel>)

    @Query("delete from email_addresses")
    fun deleteAllEntries()
}