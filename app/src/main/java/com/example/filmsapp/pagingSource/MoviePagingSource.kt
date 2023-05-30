package com.example.filmsapp.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.Movies
import com.example.filmsapp.retrofit2.dataClases.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

class MoviePagingSource(private val retrofitParse: RetrofitParse) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {//где хранится params????
        return try {
            val page = params.key ?: 1
            var movies: Movies? = null
            val latch = CountDownLatch(1)

            retrofitParse.getPopularMovies(page) { response ->
                movies = response
                latch.countDown()
            }

            withContext(Dispatchers.IO) {
                latch.await()//можно ли это совместить с предыдущем????
            }

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (movies?.results?.isNotEmpty() == true) page + 1 else null

            LoadResult.Page(
                data = movies?.results ?: emptyList(),
                prevKey = prevKey,//где хранится????
                nextKey = nextKey//где хранится????
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }



    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {//когда это запускается ????
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
