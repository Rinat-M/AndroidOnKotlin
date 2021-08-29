package com.rino.moviedb.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val movieId: Long
)