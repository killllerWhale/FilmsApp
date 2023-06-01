package com.example.filmsapp.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.dataClases.MovieItem

class MoviePagingSource(private val retrofitParse: RetrofitParse) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val page = params.key ?: 1
            val response = retrofitParse.getPopularMovies(page)

            LoadResult.Page(
                data = response.results,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.results.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
