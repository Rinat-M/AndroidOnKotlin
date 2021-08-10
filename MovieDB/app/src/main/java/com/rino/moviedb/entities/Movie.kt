package com.rino.moviedb.entities

import java.util.*

data class Movie(
    val id: Int,
    val posterPath: String,
    val adult: Boolean,
    val overview: String,
    val releaseDate: Date,
    val genreIds: List<Int>,
    val originalTitle: String,
    val originalLanguage: String,
    val title: String,
    val backdropPath: String,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean,
    val voteAverage: Double
)