package com.rino.moviedb.database

import android.util.Log
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rino.moviedb.BuildConfig
import java.lang.Exception

private const val TAG = "Migrations"

class MigrationFrom1To2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        Log.i(TAG, "Migration database from $startVersion to $endVersion")

        try {
            database.beginTransaction()

            database.execSQL("ALTER TABLE Movie ADD COLUMN 'budget' INTEGER")
            database.execSQL("ALTER TABLE Movie ADD COLUMN 'imdbId' TEXT")
            database.execSQL("ALTER TABLE Movie ADD COLUMN 'revenue' INTEGER")
            database.execSQL("ALTER TABLE Movie ADD COLUMN 'tagline' TEXT")
            database.execSQL("ALTER TABLE Movie ADD COLUMN 'director' TEXT")

            database.execSQL(
                """CREATE TABLE Actor (
                id INTEGER NOT NULL,
                adult INTEGER NOT NULL,
                gender INTEGER NOT NULL,
                knownForDepartment TEXT,
                name TEXT NOT NULL,
                originalName TEXT NOT NULL,
                popularity REAL NOT NULL,
                profilePath TEXT,
                castId INTEGER NOT NULL,
                character TEXT NOT NULL,
                creditId TEXT NOT NULL,
                `order` INTEGER NOT NULL,
                PRIMARY KEY(id))"""
            )

            database.execSQL(
                """CREATE TABLE MovieActorCrossRef (
                    movieId INTEGER NOT NULL,
                    actorId INTEGER NOT NULL,
                    PRIMARY KEY(movieId, actorId)
                    )"""
            )

            database.setTransactionSuccessful()

            Log.i(
                TAG,
                "Migration from $startVersion to $endVersion version completed successfully!"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error while migrating from $startVersion to $endVersion!", e)

            if (BuildConfig.DEBUG)
                throw e
        } finally {
            database.endTransaction()
        }
    }

}
