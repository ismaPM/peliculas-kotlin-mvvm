package com.example.peliculas.data.model.list_movies.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.peliculas.data.model.list_movies.DataConverter
import com.google.gson.annotations.SerializedName
import com.example.peliculas.utils.Constants

data class MovieResponse (

    //ERROR EN SERVICIO
    @field:SerializedName("status_code")
    val statusCode: Int? = null,

    @field:SerializedName("status_message")
    val statusMessage: String? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    //NO SE ENCUENTRAN IDS
    @field:SerializedName("errors")
    val errors: List<String>? = null,

    //RESPUESTA CORRECTA
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("poster_path")
    var posterPath: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("description")
    var description: String = "",


    @SerializedName("total_results")
    var totalResults: Long = 0,


    @SerializedName("total_pages")
    var totalPages: Int = 0,

    @SerializedName("page")
    var page: Int = 0,

    @field:SerializedName("runtime")
    val runtime: Int = 0,

    @field:SerializedName("results")
    val results: List<Movie> = arrayListOf(),
)

@Entity(tableName = Constants.TABLE_MOVIE)
data class Movie(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    @field:SerializedName("id")
    var id: Long = 0,

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="poster_path")
    @field:SerializedName("poster_path")
    var posterPath: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="adult")
    @field:SerializedName("adult")
    var adult: Boolean = false,

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="overview")
    @field:SerializedName("overview")
    var overview: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="release_date")
    @field:SerializedName("release_date")
    var releaseDate: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="original_title")
    @field:SerializedName("original_title")
    var originalTitle: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="media_type")
    @field:SerializedName("media_type")
    var mediaType: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="original_language")
    @field:SerializedName("original_language")
    var originalLanguage: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="title")
    @field:SerializedName("title")
    var title: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="backdrop_path")
    @field:SerializedName("backdrop_path")
    var backdropPath: String = "",

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="popularity")
    @field:SerializedName("popularity")
    var popularity: Double = 0.0,

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="vote_count")
    @field:SerializedName("vote_count")
    var voteCount: Long = 0,

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="video")
    @field:SerializedName("video")
    var video: Boolean = false,

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name="vote_average")
    @field:SerializedName("vote_average")
    var voteAverage: Double = 0.0
)