package com.rino.moviedb.ui.details

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentMovieDetailsBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.utils.openAppSystemSettings
import com.rino.moviedb.utils.processFavorite
import com.rino.moviedb.utils.showSnackBar
import com.rino.moviedb.utils.toString
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.Manifest
import com.rino.moviedb.ui.contacts.ContactsFragment

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

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                openShareWithContacts()
            } else {
                binding.root.showSnackBar(
                    R.string.need_permissions_to_read_contacts,
                    actionStringId = R.string.settings,
                    action = { context?.openAppSystemSettings() }
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let { movieId = it.getLong(MOVIE_ID_ARG) }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.share_menu -> {
            checkPermission()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkPermission() {
        context?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) -> {
                    openShareWithContacts()
                }

                else -> permissionResult.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun openShareWithContacts() {
        val bundle = Bundle().apply {
            putString(ContactsFragment.MESSAGE_ARG, detailsViewModel.messageToShare)
        }

        findNavController().navigate(
            R.id.action_navigation_details_to_contacts,
            bundle
        )
    }

    override fun onDestroyView() {
        detailsViewModel.saveNote(movieId, binding.movieNote.text.toString())

        super.onDestroyView()
        _binding = null
    }
}