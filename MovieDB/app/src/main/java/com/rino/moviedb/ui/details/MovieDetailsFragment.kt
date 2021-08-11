package com.rino.moviedb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rino.moviedb.databinding.FragmentMovieDetailsBinding
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.utils.toString

class MovieDetailsFragment : Fragment() {
    companion object {
        const val MOVIE_ARG = "MOVIE_ARG"

        fun newInstance(movie: Movie) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MOVIE_ARG, movie)
                }
            }
    }

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getParcelable(MOVIE_ARG) as? Movie
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie?.let {
            with(binding) {
                movieTitle.text = it.title
                movieReleaseDate.text = it.releaseDate.toString("yyyy-MM-dd")
                movieOverview.text = it.overview
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}