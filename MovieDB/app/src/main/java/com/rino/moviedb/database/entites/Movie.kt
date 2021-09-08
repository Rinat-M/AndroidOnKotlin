package com.rino.moviedb.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Movie(
    @PrimaryKey val id: Long,
    val posterPath: String,
    val adult: Boolean,
    val overview: String,
    val releaseDate: Date,
    val originalTitle: String,
    val originalLanguage: String,
    val title: String,
    val backdropPath: String,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean,
    val voteAverage: Double,
    val budget: Long? = null,
    val imdbId: String? = null,
    val revenue: Long? = null,
    val tagline: String? = null,
    val director: String? = null
)