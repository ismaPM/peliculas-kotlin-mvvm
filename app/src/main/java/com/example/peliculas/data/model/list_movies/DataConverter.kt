package com.example.peliculas.data.model.list_movies

import androidx.room.TypeConverter
import com.example.peliculas.data.model.list_movies.response.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    @TypeConverter
    fun stringToMoviesModel(listString: String): List<Movie> {
        val gson = Gson()
        val type = object : TypeToken<List<Movie>>() {}.type
        return gson.fromJson(listString, type)
    }
}