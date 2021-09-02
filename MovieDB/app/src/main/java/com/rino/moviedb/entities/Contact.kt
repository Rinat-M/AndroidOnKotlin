package com.rino.moviedb.entities

data class Contact(
    val id: String,
    val displayName: String,
    val photoThumbUri: String?,
    val hasPhoneNumber: Boolean,
    val phoneNumber: String?
)