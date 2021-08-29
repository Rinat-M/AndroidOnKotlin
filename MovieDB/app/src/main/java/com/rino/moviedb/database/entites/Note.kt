package com.rino.moviedb.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey val movieId: Long,
    val note: String
)