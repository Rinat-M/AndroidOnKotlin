package com.rino.moviedb.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val movieId: Long,
    val viewingDate: Date = Date()
)