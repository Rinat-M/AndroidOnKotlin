package com.rino.moviedb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.database.entites.Favorite
import com.rino.moviedb.database.entites.MovieExtended
import com.rino.moviedb.database.entites.Note
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<MovieExtended>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<MovieExtended>> = _state

    var messageToShare: String? = null

    fun fetchData(movieId: Long) {
        viewModelScope.launch {
            val item =
                withContext(Dispatchers.IO) { moviesRepository.getMovieExtendedById(movieId) }

            item?.let {
                messageToShare = "${item.movie.title} ${BuildConfig.URL_TO_SHARE}${item.movie.id}"
                _state.value = ScreenState.Success(item)
            }
        }
    }

    fun saveNote(movieId: Long, note: String) = viewModelScope.launch(Dispatchers.IO) {
        moviesRepository.saveNote(Note(movieId, note))
    }

    fun onFavoriteEvent(movieExtended: MovieExtended) {
        viewModelScope.launch(Dispatchers.IO) {
            if (movieExtended.isFavorite) {
                moviesRepository.addMovieToFavorite(Favorite(movieId = movieExtended.movie.id))
            } else {
                moviesRepository.removeMovieFromFavorite(movieExtended.movie.id)
            }
        }
    }

}