package com.rino.moviedb.di

import com.rino.moviedb.ui.favorites.FavoritesViewModel
import com.rino.moviedb.ui.home.HomeViewModel
import com.rino.moviedb.ui.ratings.RatingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { FavoritesViewModel() }
    viewModel { RatingsViewModel() }
}