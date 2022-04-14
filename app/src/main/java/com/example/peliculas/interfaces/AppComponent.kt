package com.example.peliculas.interfaces

import com.example.peliculas.ui.main.view.activities.detalle_movie.DetalleMovieActivity
import com.example.peliculas.ui.main.view.activities.movie_list.MovieActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(movieActivity: MovieActivity)
    fun inject(detalleMovieActivity: DetalleMovieActivity)
}