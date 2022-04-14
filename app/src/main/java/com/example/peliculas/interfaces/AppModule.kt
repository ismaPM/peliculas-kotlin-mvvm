package com.example.peliculas.interfaces

import com.example.peliculas.data.model.list_movies.response.Movie
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesMovie(): Movie = Movie()
}