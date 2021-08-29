package com.rino.moviedb.entities

sealed class ScreenWithListState {
    data class Success<T>(val items: List<T>) : ScreenWithListState()
    object Loading : ScreenWithListState()
    data class Error(val error: Throwable) : ScreenWithListState()
}