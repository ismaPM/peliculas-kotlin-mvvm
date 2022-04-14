package com.example.peliculas.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.peliculas.data.dao.MoviesDAO
import com.example.peliculas.data.model.list_movies.DataConverter
import com.example.peliculas.data.model.list_movies.response.Movie

@Database(entities = [Movie::class], version =1, exportSchema = true)
@TypeConverters(DataConverter::class)
abstract class MoviesDB: RoomDatabase() {
    abstract fun moviesDao(): MoviesDAO
}