package com.rino.moviedb.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.rino.moviedb.database.entites.HistoryWithMovie

@Dao
interface MovieGetDao {

    @Query("SELECT id, viewingDate, movieId FROM History ORDER BY viewingDate DESC")
    fun getHistoryWithMovies(): List<HistoryWithMovie>
}