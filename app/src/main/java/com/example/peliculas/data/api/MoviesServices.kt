package com.example.peliculas.data.api

import com.example.peliculas.BuildConfig
import com.example.peliculas.data.model.list_movies.response.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesServices {

    @Headers("Content-Type: application/json;charset=utf-8")
    @GET("list/{list_id}?api_key=${BuildConfig.key_movies}")
    fun getMovieList(@Path("list_id") list_id: Int,@Query("page") page: Int): Single<MovieResponse>


}