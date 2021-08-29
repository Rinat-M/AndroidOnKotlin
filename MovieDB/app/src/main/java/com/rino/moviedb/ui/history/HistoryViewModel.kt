package com.rino.moviedb.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rino.moviedb.entities.ScreenWithListState
import com.rino.moviedb.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val uiScope = MainScope()

    private val _state: MutableLiveData<ScreenWithListState> =
        MutableLiveData(ScreenWithListState.Loading)
    val state: LiveData<ScreenWithListState> = _state

    init {
        uiScope.launch {
            val items = withContext(Dispatchers.IO) { moviesRepository.getHistoryWithMovies() }
            _state.value = ScreenWithListState.Success(items)
        }
    }
}