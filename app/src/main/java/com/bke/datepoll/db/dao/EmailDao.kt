package com.bke.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bke.datepoll.db.model.EmailAddressDbModel

@Dao
interface EmailDao {

    @Query("select * from email_addresses where user_id = :id")
    fun getEmailsOfUser(id: Long): LiveData<List<EmailAddressDbModel>>

    @Insert
    fun addEmails(emails: List<EmailAddressDbModel>)
}