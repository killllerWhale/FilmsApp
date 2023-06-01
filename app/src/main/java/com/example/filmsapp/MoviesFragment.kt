package com.example.filmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import com.example.filmsapp.adapter.MoviesAdapter
import com.example.filmsapp.databinding.FragmentMoviesBinding
import com.example.filmsapp.pagingSource.MoviePagingSource
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val retrofitParse = RetrofitParse()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        val imageLoader = ImageLoader.Builder(requireContext())
            .build()
        val movieAdapter = MoviesAdapter({ movie ->
            //gg
        }, imageLoader)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = movieAdapter
        val moviesFlow: Flow<PagingData<MovieItem>> = Pager(config = PagingConfig(pageSize = 20)) {
            MoviePagingSource(retrofitParse)
        }.flow

        lifecycleScope.launch {
            moviesFlow.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}