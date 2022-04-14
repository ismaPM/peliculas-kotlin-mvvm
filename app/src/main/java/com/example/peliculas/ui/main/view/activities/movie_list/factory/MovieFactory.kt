package com.example.peliculas.ui.main.view.activities.movie_list.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.peliculas.data.db.database.DatabaseHelper
import com.example.peliculas.data.repository.list_movies.MovieRepository
import com.example.peliculas.ui.main.view.activities.movie_list.viewmodel.MovieViewModel

@Suppress("UNCHECKED_CAST")
class MovieFactory(
    private val movieRepository: MovieRepository,
    private val dbHelper: DatabaseHelper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            MovieViewModel(
                movieRepository = this.movieRepository,
                dbHelper = dbHelper) as T
        } else throw IllegalArgumentException("Class MovieViewModel not found")
    }
}