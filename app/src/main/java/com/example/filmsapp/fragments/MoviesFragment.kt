package com.example.filmsapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.filmsapp.R
import com.example.filmsapp.adapter.MoviesAdapter
import com.example.filmsapp.databinding.FragmentMoviesBinding
import com.example.filmsapp.pagingSource.MoviePagingSource
import com.example.filmsapp.pagingSource.MovieSearchPagingSource
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MoviesFragment : ViewBindingFragment<FragmentMoviesBinding>() {
    private val retrofitParse = RetrofitParse()
    private lateinit var movieAdapter: MoviesAdapter

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviesBinding {
        return FragmentMoviesBinding.inflate(inflater)
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MoviesAdapter(requireContext()) { movie ->
            loadFragment(movie)
        }

        binding.recyclerView.adapter = movieAdapter
        loadData(MoviePagingSource(retrofitParse))
        searchViewCreated()
    }

    private fun searchViewCreated() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                loadData(MovieSearchPagingSource(retrofitParse, query))
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun loadData(pagingSource: PagingSource<Int, MovieItem>) {
        val moviesFlow: Flow<PagingData<MovieItem>> = Pager(config = PagingConfig(pageSize = 20)) {
            pagingSource
        }.flow

        CoroutineScope(Dispatchers.Main).launch {
            moviesFlow.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }

    private fun loadFragment(movie: MovieItem) {
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        findNavController().navigate(R.id.movieFragment2, bundle)
    }
}