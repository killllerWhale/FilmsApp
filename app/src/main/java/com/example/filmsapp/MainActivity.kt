package com.example.filmsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.filmsapp.retrofit2.RetrofitParse


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitParse().getPopularMovie(1) { movies ->
            println(movies.results[0].original_title)
            println(movies.results[0].id)
            RetrofitParse().getMovieById(movies.results[0].id) { movie ->
                println(movie.belongs_to_collection!!.name)
                println(movie.overview)
            }
        }
    }
}