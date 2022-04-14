package com.example.peliculas.data.db.database

import androidx.lifecycle.LiveData
import com.example.peliculas.data.db.MoviesDB
import com.example.peliculas.data.model.list_movies.response.Movie
import io.reactivex.Flowable

class DatabaseHelperImpl(private val db: MoviesDB) : DatabaseHelper {

    override suspend fun insertarMovies(movies: List<Movie>) {
        db.moviesDao().insertMovies(movies)
    }

    override suspend fun selectMovies(): Flowable<List<Movie>> =
        db.moviesDao().selectMovies()

    override suspend fun deleteMovies() {
        db.moviesDao().deleteMovies()
    }

}