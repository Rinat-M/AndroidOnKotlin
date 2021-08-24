package com.rino.moviedb.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentHomeBinding
import com.rino.moviedb.entities.AppState
import com.rino.moviedb.entities.CategoryWithMovies
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.MoviesCategory
import com.rino.moviedb.services.MovieDownloadService
import com.rino.moviedb.ui.details.MovieDetailsFragment
import com.rino.moviedb.ui.home.adapters.CategoryWithMoviesAdapter
import com.rino.moviedb.ui.home.adapters.MoviesAdapter
import com.rino.moviedb.utils.showSnackBar
import com.rino.moviedb.utils.showToast

class HomeWithServiceFragment : Fragment() {

    companion object {
        const val HOME_WITH_SERVICE_INTENT_FILTER = "HOME_WITH_SERVICE_INTENT_FILTER"
        const val NOW_PLAYING_MOVIES_PARAMETER = "NOW_PLAYING_MOVIES_PARAMETER"
        const val UPCOMING_MOVIES_PARAMETER = "UPCOMING_MOVIES_PARAMETER"
    }

    private val movieDownloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val nowPlayingMovies =
                intent?.getParcelableArrayListExtra<Movie>(NOW_PLAYING_MOVIES_PARAMETER)
                    ?: arrayListOf()
            val upcomingMovies =
                intent?.getParcelableArrayListExtra<Movie>(UPCOMING_MOVIES_PARAMETER)
                    ?: arrayListOf()

            processData(AppState.Success(nowPlayingMovies, upcomingMovies))

            context?.showToast(R.string.downloaded_from_service)
        }
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager
                .getInstance(it)
                .registerReceiver(
                    movieDownloadReceiver,
                    IntentFilter(HOME_WITH_SERVICE_INTENT_FILTER)
                )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MovieDownloadService.start(requireContext())
    }

    private fun processData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Loading -> {
                progressBar.isVisible = true
                categoriesRecyclerview.isVisible = false

                mainConstraint.showSnackBar(R.string.loading)
            }

            is AppState.Success -> {
                progressBar.isVisible = false
                categoriesRecyclerview.isVisible = true

                val onItemClickListener = object : MoviesAdapter.OnItemClickListener {
                    override fun onItemClick(movie: Movie) {
                        val navController = findNavController()

                        val bundle = Bundle().apply {
                            putParcelable(MovieDetailsFragment.MOVIE_ARG, movie)
                        }

                        navController.navigate(R.id.action_navigation_home_to_movie_details, bundle)
                    }
                }

                val categoriesWithMovies = listOf(
                    CategoryWithMovies(
                        MoviesCategory.NOW_PLAYING,
                        getString(R.string.now_playing),
                        appState.nowPlaying
                    ),
                    CategoryWithMovies(
                        MoviesCategory.UPCOMING,
                        getString(R.string.upcoming),
                        appState.upcomingMovies
                    )
                )

                categoriesRecyclerview.adapter =
                    CategoryWithMoviesAdapter(categoriesWithMovies, onItemClickListener)

                mainConstraint.showSnackBar(R.string.success)
            }

            is AppState.Error -> {
                progressBar.isVisible = false
                categoriesRecyclerview.isVisible = false

                with(errorMsg) {
                    isVisible = true
                    text = appState.error.message
                }

                mainConstraint.showSnackBar(appState.error.message ?: "")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        MovieDownloadService.stop(requireContext())
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(movieDownloadReceiver)
        }

        super.onDestroy()
    }
}