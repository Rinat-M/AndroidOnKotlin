package com.rino.moviedb.ui.details

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentMovieDetailsBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.Actor
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.ui.contacts.ContactsFragmentArgs
import com.rino.moviedb.ui.person.PersonFragmentArgs
import com.rino.moviedb.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : Fragment() {
    companion object {
        fun newInstance(movieId: Long) =
            MovieDetailsFragment().apply {
                arguments = MovieDetailsFragmentArgs(movieId).toBundle()
            }

        fun createDeepLink(context: Context, movieId: Long): PendingIntent {
            val arg = MovieDetailsFragmentArgs(movieId).toBundle()

            return NavDeepLinkBuilder(context)
                .setGraph(R.navigation.mobile_navigation)
                .setDestination(R.id.movie_details)
                .setArguments(arg)
                .createPendingIntent()
        }
    }

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val detailsViewModel: MovieDetailsViewModel by viewModel()

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

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

    private val onItemClickListener: ActorsAdapter.OnItemClickListener by lazy {
        object : ActorsAdapter.OnItemClickListener {
            override fun onItemClick(actorId: Long) {
                val bundle = PersonFragmentArgs(actorId).toBundle()
                findNavController().navigate(R.id.action_navigation_details_to_person, bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        detailsViewModel.fetchData(args.movieId)
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
                        val movieExtended = state.data
                        val movie = movieExtended.movie
                        val note = movieExtended.note

                        val actors = movieExtended.actors.asSequence()
                            .sortedBy { it.order }
                            .map { Actor(it.id, it.name, it.order) }
                            .take(10)
                            .toList()

                        with(binding) {
                            visibilityGroup.isVisible = true
                            includeBinding.progressBar.isVisible = false

                            Glide.with(requireContext())
                                .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.backdropPath}")
                                .placeholder(circularProgressDrawable)
                                .into(movieBanner)

                            movieTitle.text = movie.title
                            movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")
                            movieOverview.text = movie.overview

                            with(movieTagline) {
                                text = movie.tagline
                                isGone = movie.tagline.isNullOrEmpty()
                            }

                            movieDirector.text = movie.director
                            movieBudget.text = movie.budget?.formatCurrency()
                            movieId.text = movie.id.toString()

                            Glide.with(requireContext())
                                .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.posterPath}")
                                .placeholder(circularProgressDrawable)
                                .into(moviePoster)

                            movieNote.setText(note?.note)

                            movieFavorite.processFavorite(movieExtended.isFavorite)
                            movieFavorite.setOnClickListener {
                                movieExtended.isFavorite = !movieExtended.isFavorite
                                movieFavorite.processFavorite(movieExtended.isFavorite)
                                detailsViewModel.onFavoriteEvent(movieExtended)
                            }

                            with(movieActorsRecyclerView) {
                                layoutManager = FlexboxLayoutManager(context)
                                adapter = ActorsAdapter(actors, onItemClickListener)
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
        val bundle = ContactsFragmentArgs(detailsViewModel.messageToShare).toBundle()

        findNavController().navigate(
            R.id.action_navigation_details_to_contacts,
            bundle
        )
    }

    override fun onDestroyView() {
        detailsViewModel.saveNote(args.movieId, binding.movieNote.text.toString())

        super.onDestroyView()
        _binding = null
    }
}