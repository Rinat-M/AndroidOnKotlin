package com.rino.moviedb.remote.entites

import com.google.gson.annotations.SerializedName

data class CastDTO(
    val id: Long,
    val adult: Boolean,
    val gender: Int,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String,
    @SerializedName("cast_id")
    val castId: Long,
    val character: String,
    @SerializedName("credit_id")
    val creditId: String,
    val order: Int
)