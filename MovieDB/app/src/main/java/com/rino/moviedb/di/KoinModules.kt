package com.rino.moviedb.di

import com.rino.moviedb.database.DatabaseModule
import com.rino.moviedb.datasources.DataSource
import com.rino.moviedb.datasources.RemoteDataSourceImpl
import com.rino.moviedb.helpers.NotificationHelper
import com.rino.moviedb.repositories.MoviesRepository
import com.rino.moviedb.repositories.MoviesRepositoryImpl
import com.rino.moviedb.ui.contacts.ContactsViewModel
import com.rino.moviedb.ui.details.MovieDetailsViewModel
import com.rino.moviedb.ui.favorites.FavoritesViewModel
import com.rino.moviedb.ui.history.HistoryViewModel
import com.rino.moviedb.ui.home.HomeViewModel
import com.rino.moviedb.ui.map.MapViewModel
import com.rino.moviedb.ui.person.PersonViewModel
import com.rino.moviedb.ui.ratings.RatingsViewModel
import com.rino.moviedb.ui.settings.SettingsViewModel
import com.rino.moviedb.wrappers.MainSharedPreferencesWrapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DataSource> { RemoteDataSourceImpl(get()) }
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get(), get()) }

    // Network
    single { NetworkModule.getOkHttpClient() }
    single { NetworkModule.getRetrofit(get()) }
    single { NetworkModule.getMovieDbService(get()) }

    // SharedPreferences
    single { MainSharedPreferencesWrapper(get()) }

    // Database
    single { DatabaseModule.getAppDatabase(get()) }
    single { DatabaseModule.getMovieGetDao(get()) }
    single { DatabaseModule.getMovieSetDao(get()) }

    // Notification helper
    single { NotificationHelper(get()) }

    // view models
    viewModel { HomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { RatingsViewModel() }
    viewModel { SettingsViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { ContactsViewModel() }
    viewModel { PersonViewModel(get()) }
    viewModel { MapViewModel(get()) }
}