package com.rino.moviedb.ui.history

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.rino.moviedb.R
import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.databinding.FragmentHistoryBinding
import com.rino.moviedb.databinding.ProgressBarAndErrorMsgBinding
import com.rino.moviedb.entities.ScreenState
import com.rino.moviedb.ui.details.MovieDetailsFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    private fun processData(state: ScreenState<List<HistoryWithMovie>>) {
        when (state) {
            ScreenState.Loading -> {
                includeBinding.progressBar.isVisible = true
                binding.historyRecyclerView.isVisible = false
            }

            is ScreenState.Success -> {
                includeBinding.progressBar.isVisible = false

                with(binding.historyRecyclerView) {
                    isVisible = true
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    val onItemClickListener = object : HistoryAdapter.OnItemClickListener {
                        override fun onItemClick(movieId: Long) {
                            val bundle = MovieDetailsFragmentArgs(movieId).toBundle()

                            findNavController().navigate(
                                R.id.action_navigation_history_to_movie_details,
                                bundle
                            )
                        }
                    }

                    adapter = HistoryAdapter(
                        requireContext(),
                        state.data,
                        onItemClickListener
                    )
                }
            }

            is ScreenState.Error -> {
                includeBinding.progressBar.isVisible = false
                binding.historyRecyclerView.isVisible = false

                with(includeBinding.errorMsg) {
                    isVisible = true
                    text = state.error.message
                }

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)

        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(query: String?): Boolean {
                historyViewModel.searchByQuery(query)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        historyViewModel.searchByQuery(null)
        _binding = null
    }

}