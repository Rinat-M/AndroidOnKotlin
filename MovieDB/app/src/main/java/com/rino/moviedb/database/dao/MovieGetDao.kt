package com.rino.moviedb.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.database.entites.Movie
import com.rino.moviedb.database.entites.MovieWithNote

@Dao
interface MovieGetDao {

    @Query("SELECT id, viewingDate, movieId FROM History ORDER BY viewingDate DESC")
    fun getHistoryWithMovies(): List<HistoryWithMovie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun getMovieById(id: Long): Movie

    @Query("SELECT id FROM Movie WHERE id = :id")
    fun getMovieWithNoteById(id: Long): MovieWithNote

}