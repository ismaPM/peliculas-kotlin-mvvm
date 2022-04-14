package com.example.peliculas.data.db.database

import com.example.peliculas.data.model.list_movies.response.Movie
import io.reactivex.Flowable

interface DatabaseHelper {

    /**
     * Movies Relacion
     */
    suspend fun insertarMovies(movies: List<Movie>)
    suspend fun selectMovies(): Flowable<List<Movie>>
    suspend fun deleteMovies()
}