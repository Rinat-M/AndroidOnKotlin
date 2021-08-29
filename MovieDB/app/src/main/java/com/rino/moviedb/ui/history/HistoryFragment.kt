package com.rino.moviedb.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.databinding.FragmentHistoryBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.ScreenWithListState
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let { processData(it) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun processData(state: ScreenWithListState) {
        when (state) {
            ScreenWithListState.Loading -> {
                includeBinding.progressBar.isVisible = true
                binding.historyRecyclerView.isVisible = false
            }

            is ScreenWithListState.Success<*> -> {
                includeBinding.progressBar.isVisible = false

                with(binding.historyRecyclerView) {
                    isVisible = true
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    adapter = HistoryAdapter(
                        requireContext(),
                        state.items as List<HistoryWithMovie>
                    )
                }
            }

            is ScreenWithListState.Error -> {
                includeBinding.progressBar.isVisible = false
                binding.historyRecyclerView.isVisible = false

                with(includeBinding.errorMsg) {
                    isVisible = true
                    text = state.error.message
                }

            }

        }
    }

}