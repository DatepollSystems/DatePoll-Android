package com.datepollsystems.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.datepollsystems.datepoll.data.MovieDbModel

@Dao
interface CinemaDao {

    @Query("select * from movies")
    fun loadAllMovies(): LiveData<List<MovieDbModel>>

    @Query("select inserted_at from movies limit 1")
    fun getInsertionTime(): Long

    @Query("select count(*) from movies")
    fun countAllMovies(): Long

    @Query("select * from movies where booked_tickets_for_yourself > 0")
    fun selectBookedMovies(): LiveData<List<MovieDbModel>>

    @Insert
    fun insertAllMovieDbModel(list: List<MovieDbModel>)

    @Insert
    fun insertMovie(m: MovieDbModel)

    @Update
    fun updateMovie(m: MovieDbModel)

    @Query("delete from movies")
    fun deleteAll()

    fun insertAndDelete(list: List<MovieDbModel>){
        deleteAll()
        insertAllMovieDbModel(list)
    }
}