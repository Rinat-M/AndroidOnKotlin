package com.rino.moviedb.database

import android.content.Context
import androidx.room.Room
import com.rino.moviedb.database.dao.MovieGetDao
import com.rino.moviedb.database.dao.MovieSetDao

object DatabaseModule {
    private const val DATABASE_NAME = "movies_database.db"

    private val migrationFrom1To2 by lazy { MigrationFrom1To2() }

    fun getAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addMigrations(migrationFrom1To2)
            .build()

    fun getMovieGetDao(appDatabase: AppDatabase): MovieGetDao = appDatabase.getMovieGetDao()

    fun getMovieSetDao(appDatabase: AppDatabase): MovieSetDao = appDatabase.getMovieSetDao()
}