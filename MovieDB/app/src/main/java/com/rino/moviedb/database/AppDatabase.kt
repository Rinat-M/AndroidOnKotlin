package com.rino.moviedb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rino.moviedb.database.converters.DateConverter
import com.rino.moviedb.database.dao.MovieGetDao
import com.rino.moviedb.database.dao.MovieSetDao
import com.rino.moviedb.database.entites.Favorite
import com.rino.moviedb.database.entites.History
import com.rino.moviedb.database.entites.Movie
import com.rino.moviedb.database.entites.Note

@Database(
    entities = [
        Movie::class, History::class, Note::class, Favorite::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieGetDao(): MovieGetDao
    abstract fun getMovieSetDao(): MovieSetDao
}