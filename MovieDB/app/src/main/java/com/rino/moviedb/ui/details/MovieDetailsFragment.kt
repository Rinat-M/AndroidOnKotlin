package com.rino.moviedb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.databinding.FragmentMovieDetailsBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.utils.processFavorite
import com.rino.moviedb.utils.toString
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : Fragment() {
    companion object {
        const val MOVIE_ID_ARG = "MOVIE_ID_ARG"

        fun newInstance(movieId: Long) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(MOVIE_ID_ARG, movieId)
                }
            }
    }

    private val detailsViewModel: MovieDetailsViewModel by viewModel()

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private var movieId: Long = 0

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(requireContext()).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getLong(MOVIE_ID_ARG)
        }

        detailsViewModel.fetchData(movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    ScreenState.Loading -> {
                        binding.visibilityGroup.isVisible = false
                        includeBinding.progressBar.isVisible = true
                    }

                    is ScreenState.Success -> {
                        val movieWithNote = state.data
                        val movie = movieWithNote.movie
                        val note = movieWithNote.note

                        with(binding) {
                            visibilityGroup.isVisible = true
                            includeBinding.progressBar.isVisible = false

                            movieTitle.text = movie.title
                            movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")
                            movieOverview.text = movie.overview

                            Glide.with(requireContext())
                                .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.posterPath}")
                                .centerCrop()
                                .placeholder(circularProgressDrawable)
                                .into(moviePoster)

                            movieNote.setText(note?.note)

                            movieFavorite.processFavorite(movieWithNote.isFavorite)
                            movieFavorite.setOnClickListener {
                                movieWithNote.isFavorite = !movieWithNote.isFavorite
                                movieFavorite.processFavorite(movieWithNote.isFavorite)
                                detailsViewModel.onFavoriteEvent(movieWithNote)
                            }
                        }
                    }

                    is ScreenState.Error -> {
                        binding.visibilityGroup.isVisible = false

                        with(includeBinding) {
                            progressBar.isVisible = false
                            errorMsg.isVisible = true
                            errorMsg.text = state.error.toString()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        detailsViewModel.saveNote(movieId, binding.movieNote.text.toString())

        super.onDestroyView()
        _binding = null
    }
}