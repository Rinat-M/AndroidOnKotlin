package com.rino.moviedb.di

import com.rino.moviedb.datasources.DataSource
import com.rino.moviedb.datasources.RemoteDataSourceImpl
import com.rino.moviedb.repositories.MoviesRepository
import com.rino.moviedb.repositories.MoviesRepositoryImpl
import com.rino.moviedb.ui.favorites.FavoritesViewModel
import com.rino.moviedb.ui.home.HomeViewModel
import com.rino.moviedb.ui.ratings.RatingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DataSource> { RemoteDataSourceImpl(get()) }
    single<MoviesRepository> { MoviesRepositoryImpl(get()) }

    single { NetworkModule.getOkHttpClient() }
    single { NetworkModule.getRetrofit(get()) }
    single { NetworkModule.getMovieDbService(get()) }

    // view models
    viewModel { HomeViewModel(get()) }
    viewModel { FavoritesViewModel() }
    viewModel { RatingsViewModel() }
}