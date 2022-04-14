package com.example.peliculas.data.repository.list_movies

import com.example.peliculas.data.api.MoviesServices
import com.example.peliculas.data.model.list_movies.response.MovieResponse
import io.reactivex.Single

class MovieRepository(private val moviesServices: MoviesServices) {

    fun getMovies(page: Int): Single<MovieResponse> =
        moviesServices.getMovieList(1,page)

}