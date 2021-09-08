package com.rino.moviedb.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Actor(
    @PrimaryKey val id: Long,
    val adult: Boolean,
    val gender: Int,
    val knownForDepartment: String?,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?,
    val castId: Long,
    val character: String,
    val creditId: String,
    val order: Int
)