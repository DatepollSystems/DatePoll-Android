package com.datepollsystems.datepoll.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datepollsystems.datepoll.data.MovieDbModel
import com.datepollsystems.datepoll.data.MovieOrder
import com.datepollsystems.datepoll.data.MovieOrderTupl
import kotlinx.coroutines.flow.Flow

@Dao
interface CinemaDao {

    @Query("select * from movies")
    fun loadAllMovies(): Flow<List<MovieDbModel>>

    @Query("select * from movies where id = :id limit 1")
    fun loadMovieById(id: Long): MovieDbModel

    @Query("select * from movies where id = :id limit 1")
    fun loadMovieByIdFlow(id: Long): LiveData<MovieDbModel>

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
    fun deleteAllMovies()

    @Transaction
    fun insertAndDelete(list: List<MovieDbModel>) {
        deleteAllMovies()
        insertAllMovieDbModel(list)
    }

    @Insert
    fun insertAllOrders(list: List<MovieOrder>)

    @Insert
    fun insertOrder(order: MovieOrder)

    @Query("delete from movie_orders")
    fun deleteAllOrders()

    @Transaction
    @Query("select * from movies m WHERE m.emergency_worker_id = (select id from users limit 1) OR m.worker_id = (select id from users limit 1)")
    fun getAllMoviesWhereCinemaWorker(): Flow<List<MovieDbModel>>

    @Transaction
    @Query("select * from movie_orders where movie_id = :movieId")
    fun getAllOrderForMovie(movieId: Long): Flow<List<MovieOrder>>

    @Query("select count(*) from movie_orders")
    fun countOrders(): Long
}