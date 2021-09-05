package com.rino.moviedb.database.dao

import androidx.room.*
import com.rino.moviedb.database.entites.*

@Dao
interface MovieSetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToFavourite(favorite: Favorite)

    @Query("DELETE FROM Favorite where movieId = :movieId")
    fun deleteFromFavouriteByMovieId(movieId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToHistory(history: History)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActors(actors: List<Actor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieActorCrossRef(entities: List<MovieActorCrossRef>)

    @Transaction
    fun insertMovieActors(movieId: Long, actors: List<Actor>) {
        insertActors(actors)

        val movieActorCrossRef = actors.map { MovieActorCrossRef(movieId, it.id) }
        insertMovieActorCrossRef(movieActorCrossRef)
    }

}