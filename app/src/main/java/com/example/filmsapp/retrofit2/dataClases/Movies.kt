package com.example.filmsapp.retrofit2.dataClases

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Movies(
    val page: Int,
    val results: List<MovieItem>
)

@Parcelize
class MovieItem(
    val adult: Boolean,
    val backdrop_path: String,
    val id: Int,
    val title: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val media_type: String,
    val genre_ids: List<Int>,
    val popularity: Double,
    val release_date: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable


