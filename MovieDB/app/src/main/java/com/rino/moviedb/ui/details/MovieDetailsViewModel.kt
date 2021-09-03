package com.rino.moviedb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.database.entites.Favorite
import com.rino.moviedb.database.entites.MovieWithNote
import com.rino.moviedb.database.entites.Note
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<MovieWithNote>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<MovieWithNote>> = _state

    var messageToShare: String? = null

    fun fetchData(movieId: Long) {
        viewModelScope.launch {
            val item =
                withContext(Dispatchers.IO) { moviesRepository.getMovieWithNoteById(movieId) }

            messageToShare = "${item.movie.title} ${BuildConfig.URL_TO_SHARE}${item.movie.id}"

            _state.value = ScreenState.Success(item)
        }
    }

    fun saveNote(movieId: Long, note: String) = viewModelScope.launch(Dispatchers.IO) {
        moviesRepository.saveNote(Note(movieId, note))
    }

    fun onFavoriteEvent(movieWithNote: MovieWithNote) {
        viewModelScope.launch(Dispatchers.IO) {
            if (movieWithNote.isFavorite) {
                moviesRepository.addMovieToFavorite(Favorite(movieId = movieWithNote.id))
            } else {
                moviesRepository.removeMovieFromFavorite(movieWithNote.id)
            }
        }
    }

}