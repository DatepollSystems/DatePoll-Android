package com.datepollsystems.datepoll.database.dao

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