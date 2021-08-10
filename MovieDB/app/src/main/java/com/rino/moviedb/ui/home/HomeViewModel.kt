package com.rino.moviedb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rino.moviedb.entities.AppState
import com.rino.moviedb.repositories.MoviesRepository
import java.lang.Thread.sleep

class HomeViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _appState: MutableLiveData<AppState> = MutableLiveData(AppState.Loading)
    val appState: LiveData<AppState> = _appState

    init {
        getData()
    }

    private fun getData() {
        Thread {
            sleep(1000)

            val rand = (0..100).random()

            if (rand % 2 == 0) {
                _appState.postValue(
                    AppState.Success(
                        moviesRepository.getNowPlayingMovies(),
                        moviesRepository.getUpcomingMovies()
                    )
                )
            } else {
                _appState.postValue(AppState.Error(Exception("Error")))
            }
        }
    }
}