package com.rino.moviedb.database.entites

import androidx.room.Relation

data class MovieWithNote(
    val id: Long,
    var isFavorite: Boolean,
    @Relation(parentColumn = "id", entityColumn = "id")
    val movie: Movie,
    @Relation(parentColumn = "id", entityColumn = "movieId")
    val note: Note? = null
)