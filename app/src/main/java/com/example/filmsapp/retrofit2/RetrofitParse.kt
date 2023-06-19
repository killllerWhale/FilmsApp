package com.example.filmsapp.retrofit2;

import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.Movies
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query


class RetrofitParse {

    private val api: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl(RetrofitUrls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    interface MovieApi {
        @GET("movie/popular")
        suspend fun getPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("page") pageNumber: Int
        ): Movies
        @GET("search/movie")
        suspend fun getSearchMovies(
            @Query("api_key") apiKey: String,
            @Query("query") query: String,
            @Query("page") pageNumber: Int
        ): Movies

        @GET("movie/{id}")
        suspend fun getMovieById(
            @Path("id") idMovie: Int,
            @Query("api_key") apiKey: String
        ): Movie
    }

    suspend fun getPopularMovies(pageNumber: Int): Movies {
        return try {
            api.getPopularMovies(RetrofitUrls.API_KEY,pageNumber)
        } catch (e: Exception) {
            println("ggg")
            Movies(pageNumber, listOf())
        }
    }

    suspend fun getSearchMovies(query: String ,pageNumber: Int): Movies {
        return try {
            api.getSearchMovies(RetrofitUrls.API_KEY,query,pageNumber)
        } catch (e: Exception) {
            println("ggg")
            Movies(pageNumber, listOf())
        }
    }

    suspend fun getMovieById(idMovie: Int): Movie {
        return try {
            api.getMovieById(idMovie,RetrofitUrls.API_KEY)
        } catch (e: Exception) {
            println("ggg")
            Movie(false, null, null, 0, emptyList(), "", 0, "", "", "", "", 0.0, "", emptyList(), emptyList(), "", 0, 0, emptyList(), "", "", "", false, 0.0, 0)
        }
    }
}
