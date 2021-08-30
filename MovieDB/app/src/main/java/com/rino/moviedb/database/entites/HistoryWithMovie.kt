package com.rino.moviedb.database.entites

import androidx.room.Relation
import java.util.*

data class HistoryWithMovie(
    val id: Long,
    val movieId: Long,
    val viewingDate: Date,
    @Relation(parentColumn = "movieId", entityColumn = "id")
    val movie: Movie
)