package com.rino.moviedb.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentFavoritesBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.ui.details.MovieDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private val onItemClickListener by lazy {
        object : FavoritesAdapter.OnItemClickListener {
            override fun onItemClick(movieId: Long) {
                val bundle = Bundle().apply {
                    putLong(MovieDetailsFragment.MOVIE_ID_ARG, movieId)
                }

                findNavController().navigate(
                    R.id.action_navigation_favorite_to_movie_details,
                    bundle
                )
            }
        }
    }

    private val onFavoriteClickListener by lazy {
        object : FavoritesAdapter.OnFavoriteClickListener {
            override fun onFavorite(movieId: Long, isFavorite: Boolean) {
                favoritesViewModel.onFavoriteEvent(movieId, isFavorite)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let { processData(it) }
        }
    }

    private fun processData(state: ScreenState<List<Movie>>) {
        when (state) {
            ScreenState.Loading -> {
                includeBinding.progressBar.isVisible = true
                binding.favoriteRecyclerView.isVisible = false
            }

            is ScreenState.Success -> {
                includeBinding.progressBar.isVisible = false

                with(binding.favoriteRecyclerView) {
                    isVisible = true

                    adapter = FavoritesAdapter(
                        requireContext(),
                        state.data,
                        onItemClickListener,
                        onFavoriteClickListener
                    )
                }
            }

            is ScreenState.Error -> {
                includeBinding.progressBar.isVisible = false
                binding.favoriteRecyclerView.isVisible = false

                with(includeBinding.errorMsg) {
                    isVisible = true
                    text = state.error.message
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}