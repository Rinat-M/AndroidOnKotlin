package com.rino.moviedb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rino.moviedb.entities.AppState
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.repositories.MoviesRepository
import com.rino.moviedb.wrappers.MainSharedPreferencesWrapper

class HomeViewModel(
    private val moviesRepository: MoviesRepository,
    private val mainPreferences: MainSharedPreferencesWrapper
) : ViewModel() {

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

            _appState.postValue(AppState.Success(nowPlayingMovies, upcomingMovies))
        }.start()
    }

}