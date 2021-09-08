package com.rino.moviedb.remote.entites

import com.google.gson.annotations.SerializedName
import java.util.*

data class MovieDetailsDTO(
    val id: Long,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val budget: Long,
    @SerializedName("genres")
    val genreIds: List<GenreDTO>,
    val homepage: String,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: Date,
    val revenue: Long,
    val runtime: Long,
    val status: String,
    @SerializedName("tagline")
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val credits: CreditsDTO
)