package com.datepollsystems.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datepollsystems.datepoll.data.BirthdayDbModel
import java.util.*

@Dao
interface BirthdayDao {

    @Query("select * from birthdays where name = :name & date = :date")
    fun getElementsWithSameData(name: String, date: String): List<BirthdayDbModel>

    @Query("delete from birthdays where name not in (:nameList) & date not in (:dateList)")
    fun deleteAllWhereNotInList(nameList: List<String>, dateList: List<String>)

    @Query("select inserted_at from birthdays limit 1")
    fun getInsertionTimeOfElements(): Long

    @Query("select count(*) from birthdays")
    fun getCount(): Int

    @Query("select * from birthdays where id = :id")
    fun getBirthdayById(id: Long): BirthdayDbModel

    @Query("select * from birthdays")
    fun getAllLiveData(): LiveData<List<BirthdayDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(b: BirthdayDbModel)

    @Delete
    fun deleteList(list: List<BirthdayDbModel>)

    @Update
    fun updateBirthday(b: BirthdayDbModel)

    fun insertWithChecking(b: BirthdayDbModel){
        val duplicates = getElementsWithSameData(b.name, b.date)
        when {
            duplicates.isEmpty() -> {
                insert(b)
            }
            duplicates.size == 1 -> {
                val old = duplicates[0]
                old.apply {
                    name = b.name
                    date = b.date
                    insertedAt = Date().time
                }

                updateBirthday(old)
            }
            else -> {
                deleteList(duplicates)
                insert(b)
            }
        }
    }
}