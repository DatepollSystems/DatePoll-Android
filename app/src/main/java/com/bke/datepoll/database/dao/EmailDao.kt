package com.bke.datepoll.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bke.datepoll.database.model.EmailAddressDbModel

@Dao
interface EmailDao {

    @Query("select * from email_addresses where user_id = :id")
    fun getEmailsOfUser(id: Long): LiveData<List<EmailAddressDbModel>>


    @Query("select email from email_addresses where user_id = :id")
    fun getEmailsByUser(id: Long): List<String>

    @Query("select * from email_addresses")
    fun getEmails(): LiveData<List<EmailAddressDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEmails(emails: List<EmailAddressDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEmail(email: EmailAddressDbModel)

    @Query("delete from email_addresses")
    fun deleteAllEntries()

    @Query("delete from email_addresses where email = :e")
    fun deleteEmail(e: String)
}