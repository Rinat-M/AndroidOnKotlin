package com.rino.moviedb.ui.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentRatingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingsFragment : Fragment() {

    private val ratingsViewModel: RatingsViewModel by viewModel()
    private var _binding: FragmentRatingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textNotifications.text = getString(R.string.rating_text)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}