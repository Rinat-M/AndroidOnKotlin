package com.rino.moviedb.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.databinding.FragmentPersonBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.remote.entites.PersonDTO
import com.rino.moviedb.ui.details.MovieDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonFragment : Fragment() {

    companion object {
        const val PERSON_ID_ARG = "PERSON_ID_ARG"

        fun newInstance(personId: Long) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(PERSON_ID_ARG, personId)
                }
            }
    }

    private var _binding: FragmentPersonBinding? = null
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

    private val personViewModel: PersonViewModel by viewModel()

    private var personId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { personId = it.getLong(PERSON_ID_ARG) }

        personViewModel.fetchData(personId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let{ processData(it) }
        }
    }

    private fun processData(state: ScreenState<PersonDTO>) {
        when (state) {
            ScreenState.Loading -> {
                binding.visibilityGroup.isVisible = false
                includeBinding.progressBar.isVisible = true
            }

            is ScreenState.Success -> {
                val person = state.data

                with(binding) {
                    visibilityGroup.isVisible = true
                    includeBinding.progressBar.isVisible = false

                    Glide.with(requireContext())
                        .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${person.profilePath}")
                        .placeholder(circularProgressDrawable)
                        .into(personProfile)

                    personName.text = person.name
                    personBirthday.text = person.birthday
                    personPlaceOfBirth.text = person.placeOfBirth
                    personBiography.text = person.biography
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}