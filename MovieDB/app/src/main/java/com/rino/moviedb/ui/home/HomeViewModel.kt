package com.rino.moviedb.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.moviedb.database.entites.Favorite
import com.rino.moviedb.entities.AppState
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.coreMovieModel
import com.rino.moviedb.entities.dbActorModel
import com.rino.moviedb.repositories.MoviesRepository
import com.rino.moviedb.wrappers.MainSharedPreferencesWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(
    private val moviesRepository: MoviesRepository,
    private val mainPreferences: MainSharedPreferencesWrapper
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val _appState: MutableLiveData<AppState> = MutableLiveData(AppState.Loading)
    val appState: LiveData<AppState> = _appState

    private val isAdultContentEnabled: Boolean
        get() = mainPreferences.isAdultContentEnabled

    fun fetchData() {
        Thread {
            var nowPlayingMovies = listOf<Movie>()
            moviesRepository.getNowPlayingMovies()
                .onSuccess { nowPlayingMovies = it }
                .onFailure {
                    _appState.postValue(AppState.Error(it))
                    return@Thread
                }

            var upcomingMovies = listOf<Movie>()
            moviesRepository.getUpcomingMovies()
                .onSuccess { upcomingMovies = it }
                .onFailure {
                    _appState.postValue(AppState.Error(it))
                    return@Thread
                }

            if (!isAdultContentEnabled) {
                nowPlayingMovies = nowPlayingMovies.filter { !it.adult }
                upcomingMovies = upcomingMovies.filter { !it.adult }
            }

            val favoritesMovieIds = moviesRepository.getAllFavoritesMoviesIds()

            favoritesMovieIds.forEach { favoriteMovieId ->
                val movie = nowPlayingMovies.firstOrNull { it.id == favoriteMovieId }
                movie?.let { it.isFavorite = true }
            }

            _appState.postValue(
                AppState.Success(
                    nowPlayingMovies.sortedByDescending { it.releaseDate },
                    upcomingMovies.sortedByDescending { it.releaseDate }
                )
            )
        }.start()
    }

    fun saveToHistoryAsync(movie: Movie) = viewModelScope.async(Dispatchers.IO) {
        moviesRepository.getMovieDetails(movie.id)
            .onSuccess { movieDetailsDTO ->
                movieDetailsDTO?.let {
                    val director = it.credits.crew.firstOrNull { crew -> crew.job == "Director" }
                    val coreMovie = it.coreMovieModel

                    moviesRepository.saveMovie(coreMovie.copy(director = director?.name))
                    moviesRepository.saveMovieToHistory(it.id)

                    val actors = it.credits.cast.map { castDTO -> castDTO.dbActorModel }
                    moviesRepository.insertMovieActors(coreMovie.id, actors)
                }
            }
            .onFailure { Log.e(TAG, "Error while saving movie to history!", it) }
    }

    fun onFavoriteEvent(movie: Movie, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.saveMovie(movie)

            if (isFavorite) {
                moviesRepository.addMovieToFavorite(Favorite(movieId = movie.id))
            } else {
                moviesRepository.removeMovieFromFavorite(movie.id)
            }
        }
    }
}