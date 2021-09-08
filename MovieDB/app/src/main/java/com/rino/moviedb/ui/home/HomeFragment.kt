package com.rino.moviedb.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentHomeBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.AppState
import com.rino.moviedb.entities.CategoryWithMovies
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.MoviesCategory
import com.rino.moviedb.ui.details.MovieDetailsFragment
import com.rino.moviedb.ui.home.adapters.CategoryWithMoviesAdapter
import com.rino.moviedb.ui.home.adapters.MoviesAdapter
import com.rino.moviedb.utils.showSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private val onItemClickListener by lazy {
        object : MoviesAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.saveToHistoryAsync(movie).await()

                    val bundle = Bundle().apply {
                        putLong(MovieDetailsFragment.MOVIE_ID_ARG, movie.id)
                    }

                    findNavController().navigate(
                        R.id.action_navigation_home_to_movie_details,
                        bundle
                    )
                }
            }
        }
    }

    private val onFavoriteClickListener by lazy {
        object : MoviesAdapter.OnFavoriteClickListener {
            override fun onFavorite(movie: Movie, isFavorite: Boolean) {
                homeViewModel.onFavoriteEvent(movie, isFavorite)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.appState.observe(viewLifecycleOwner) { appState ->
            appState?.let { processData(appState) }
        }

        homeViewModel.fetchData()
    }

    private fun processData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Loading -> {
                includeBinding.progressBar.isVisible = true
                categoriesRecyclerview.isVisible = false

                mainConstraint.showSnackBar(R.string.loading)
            }

            is AppState.Success -> {
                includeBinding.progressBar.isVisible = false
                categoriesRecyclerview.isVisible = true

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
                    CategoryWithMoviesAdapter(
                        categoriesWithMovies,
                        onItemClickListener,
                        onFavoriteClickListener
                    )

                mainConstraint.showSnackBar(R.string.success)
            }

            is AppState.Error -> {
                includeBinding.progressBar.isVisible = false
                categoriesRecyclerview.isVisible = false

                with(includeBinding.errorMsg) {
                    isVisible = true
                    text = appState.error.message
                }

                mainConstraint.showSnackBar(
                    appState.error.message ?: "",
                    actionStringId = R.string.reload
                ) { homeViewModel.fetchData() }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.settings_menu -> {
            findNavController().navigate(R.id.action_navigation_home_to_settings)
            true
        }

        R.id.history_menu -> {
            findNavController().navigate(R.id.action_navigation_home_to_history)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}