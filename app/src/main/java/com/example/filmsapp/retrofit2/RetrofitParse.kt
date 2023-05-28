package com.example.filmsapp.retrofit2;

import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.Movies
import retrofit2.Call;
import retrofit2.Callback
import retrofit2.Response
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
        fun getPopularMovie(
            @Query("api_key") apiKey: String,
            @Query("page") pageNumber: Int
        ): Call<Movies>

        @GET("movie/{id}")
        fun getMovieById(
            @Path("id") idMovie: Int,
            @Query("api_key") apiKey: String
        ): Call<Movie>
    }

    fun getPopularMovie(pageNumber: Int,callback: (Movies) -> Unit) {
        api.getPopularMovie(RetrofitUrls.API_KEY,pageNumber).enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null) {
                        callback(movies)
                    } else {
                        // Обработка ошибки
                    }
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

    fun getMovieById(idMovie: Int,callback: (Movie) -> Unit) {
        api.getMovieById(idMovie, RetrofitUrls.API_KEY).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        callback(movie)
                    } else {
                        // Обработка ошибки
                    }
                } else {
                    // Обработка ошибки
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

}
