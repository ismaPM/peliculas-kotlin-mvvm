package com.example.peliculas.data.db.database

import android.content.Context
import androidx.room.Room
import com.example.peliculas.data.db.MoviesDB
import com.example.peliculas.utils.Constants


object DatabaseBuilder {
    private var INSTANCE: MoviesDB? = null

    fun getInstance(context: Context): MoviesDB {
        if (INSTANCE == null) {
            synchronized(MoviesDB::class.java) {
                INSTANCE = buildRoomDB(context)
            }
        }

        return INSTANCE!!
    }

    fun destroyInstance() {
        if (INSTANCE?.isOpen == true) {
            INSTANCE?.close()
        }
        INSTANCE = null
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext, MoviesDB::class.java, Constants.NAME_DATABASE
        ).build()


}