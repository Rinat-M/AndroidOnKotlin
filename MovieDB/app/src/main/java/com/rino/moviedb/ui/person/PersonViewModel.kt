package com.rino.moviedb.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.remote.entites.PersonDTO
import com.rino.moviedb.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<PersonDTO>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<PersonDTO>> = _state

    fun fetchData(personId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.getPerson(personId)
                .onSuccess { it?.let { personDTO -> _state.postValue(ScreenState.Success(personDTO)) } }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }
}