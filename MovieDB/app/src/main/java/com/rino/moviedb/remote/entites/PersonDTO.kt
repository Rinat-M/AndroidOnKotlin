package com.rino.moviedb.remote.entites

import com.google.gson.annotations.SerializedName

data class PersonDTO(
    val id: Long,
    val birthday: String,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("deathday")
    val deathDay: String?,
    val name: String,
    @SerializedName("also_known_as")
    val alsoKnownAs: List<String>,
    val gender: Int,
    val biography: String,
    val popularity: Double,
    @SerializedName("place_of_birth")
    val placeOfBirth: String,
    @SerializedName("profile_path")
    val profilePath: String?,
    val adult: Boolean,
    @SerializedName("imdb_id")
    val imdbId: String,
    val homepage: String?
)