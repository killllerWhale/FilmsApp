package com.example.filmsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.collectLatest
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsapp.adapter.CardAdapterMovies
import com.example.filmsapp.databinding.ActivityMainBinding
import com.example.filmsapp.pagingSource.MoviePagingSource
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: CardAdapterMovies
    private val retrofitParse = RetrofitParse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadData()
    }

    private fun loadData() {
        val pagingConfig = PagingConfig(pageSize = 20)//Если страница изначально меньше то это сработает????
        val moviePagingSource = MoviePagingSource(retrofitParse)

        val moviesFlow: Flow<PagingData<Result>> = Pager(config = pagingConfig) {
            moviePagingSource
        }.flow

        lifecycleScope.launch {
            moviesFlow.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = CardAdapterMovies { movie ->
            toast(movie.title)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
        }
    }

    fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, duration).show()
}