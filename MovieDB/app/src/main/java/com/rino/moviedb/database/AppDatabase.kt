package com.rino.moviedb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rino.moviedb.database.converters.DateConverter
import com.rino.moviedb.database.dao.MovieGetDao
import com.rino.moviedb.database.dao.MovieSetDao
import com.rino.moviedb.database.entites.*

@Database(
    entities = [
        Movie::class, History::class, Note::class, Favorite::class, Actor::class, MovieActorCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieGetDao(): MovieGetDao
    abstract fun getMovieSetDao(): MovieSetDao
}