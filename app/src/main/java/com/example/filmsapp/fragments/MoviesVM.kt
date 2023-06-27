package com.example.filmsapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.filmsapp.pagingSource.MoviePagingSource
import com.example.filmsapp.pagingSource.MovieSearchPagingSource
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesVM : ViewModel() {

    private val retrofitParse = RetrofitParse()
    val moviesFlowResult = MutableLiveData<PagingData<MovieItem>>()
    val isNetworkAvailableBoolean = MutableLiveData<Boolean>()

    init {
        loadData(MoviePagingSource(retrofitParse))
    }


    fun isNetworkAvailable(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: run {
            isNetworkAvailableBoolean.value = false
            return
        }
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: run {
            isNetworkAvailableBoolean.value = false
            return
        }

        isNetworkAvailableBoolean.value =
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }


    private fun loadData(pagingSource: PagingSource<Int, MovieItem>) {
        val moviesFlow: Flow<PagingData<MovieItem>> = Pager(config = PagingConfig(pageSize = 20)) {
            pagingSource
        }.flow

        CoroutineScope(Dispatchers.Main).launch {
            moviesFlow.collectLatest { pagingData ->
                moviesFlowResult.value = pagingData
            }
        }
    }

    fun searchMovies(query: String) {
        loadData(MovieSearchPagingSource(retrofitParse, query))
    }
}