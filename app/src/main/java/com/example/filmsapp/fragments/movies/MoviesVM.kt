package com.example.filmsapp.fragments.movies

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.filmsapp.pagingSource.MoviePagingSource
import com.example.filmsapp.pagingSource.MovieSearchPagingSource
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.flow.Flow

class MoviesVM(private val retrofitParse: RetrofitParse) : ViewModel() {

    private var searchToken = ""
    val moviesFlow: Flow<PagingData<MovieItem>> = Pager(config = PagingConfig(pageSize = 20)) {
        if (searchToken.isBlank()) MoviePagingSource(retrofitParse)
        else MovieSearchPagingSource(retrofitParse, searchToken)
    }.flow

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    fun searchMovies(query: String) {
        searchToken = query
    }
}