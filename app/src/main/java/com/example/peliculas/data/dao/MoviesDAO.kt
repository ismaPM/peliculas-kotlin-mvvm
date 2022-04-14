package com.example.peliculas.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.peliculas.data.model.list_movies.response.Movie
import io.reactivex.Flowable

@Dao
interface MoviesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM data_movie")
    fun selectMovies(): Flowable<List<Movie>>

    @Query("DELETE FROM data_movie")
    suspend fun deleteMovies()

}