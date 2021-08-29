package com.rino.moviedb.database.dao

import androidx.room.*
import com.rino.moviedb.database.entites.Favorite
import com.rino.moviedb.database.entites.History
import com.rino.moviedb.database.entites.Movie
import com.rino.moviedb.database.entites.Note

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

}