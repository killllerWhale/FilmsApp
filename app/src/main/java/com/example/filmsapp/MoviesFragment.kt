package com.example.filmsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.filmsapp.adapter.MoviesAdapter
import com.example.filmsapp.databinding.FragmentMoviesBinding
import com.example.filmsapp.pagingSource.MoviePagingSource
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
        val statusBarHeight = resources.getDimensionPixelSize(
            resources.getIdentifier("status_bar_height", "dimen", "android")
        )
        binding.recyclerView.setPadding(0, statusBarHeight, 0, 0)
        movieAdapter = MoviesAdapter { movie ->
            loadFragment(MovieFragment(), movie)
        }
        binding.recyclerView.adapter = movieAdapter
        loadData()
    }

    private fun loadData() {
        val moviesFlow: Flow<PagingData<MovieItem>> = Pager(config = PagingConfig(pageSize = 20)) {
            MoviePagingSource(retrofitParse)
        }.flow

        CoroutineScope(Dispatchers.Main).launch {
            moviesFlow.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }


    private fun loadFragment(fragment: Fragment, movie: MovieItem) {
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        )
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}