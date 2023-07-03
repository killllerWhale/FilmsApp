package com.example.filmsapp.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertMovie(movie: MovieItemDb)

    @Query("SELECT * FROM movies")
    fun getAllMovie(): Flow<List<MovieItemDb>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): MovieItemDb?

    @Query("DELETE FROM movies WHERE id = :movieId")
    fun deleteMovieById(movieId: Int)
}