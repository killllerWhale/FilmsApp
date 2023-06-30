package com.example.filmsapp.fragments.movie

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmsapp.R
import com.example.filmsapp.dataBase.MainDb
import com.example.filmsapp.dataBase.MovieItemDb
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow


class MovieVM : ViewModel() {

    private lateinit var db: MainDb
    val isFavorite = MutableStateFlow(false)
    val titleText = MutableStateFlow("")
    val reviewText = MutableStateFlow("")
    val posterPath = MutableStateFlow("")
    val yearOfProductionText = MutableStateFlow("")
    val countryText = MutableStateFlow("")
    val genreText = MutableStateFlow("")
    val directorText = MutableStateFlow("")
    val sloganText = MutableStateFlow("")

    fun initializeMovie(arguments: Bundle?, context: Context) {
        db = MainDb.getDb(context)
        checkFavorite(arguments!!.getInt("movieId"))
        if (arguments.getInt("movieId") == 0) {
            initializeByParcelable(arguments)
        } else {
            initializeByDb(arguments.getInt("movieId"))
        }
    }

    private fun initializeByDb(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieItemDb = db.getDao().getMovieById(id)
            val imageUrl = movieItemDb!!.poster_path
            posterPath.value = "${RetrofitUrls.IMAGE_URL}${imageUrl}"
            yearOfProductionText.value = movieItemDb.yearOfProduction
            titleText.value = movieItemDb.title
            reviewText.value = movieItemDb.overview
            countryText.value = movieItemDb.production_countries
            genreText.value = movieItemDb.genres
            directorText.value = movieItemDb.production_companies
            sloganText.value = movieItemDb.tagline
        }
    }

    private fun initializeByParcelable(arguments: Bundle?) {
        val movie: MovieItem? = arguments!!.parcelable("movie")
        titleText.value = movie!!.title
        reviewText.value = movie.overview
        posterPath.value = "${RetrofitUrls.IMAGE_URL}${movie.poster_path}"

        viewModelScope.launch(Dispatchers.IO) {
            val movieInDetail: Movie = RetrofitParse().getMovieById(movie.id)
            yearOfProductionText.value = movieInDetail.release_date
            countryText.value =
                movieInDetail.production_countries.joinToString(", ") { it.name }
            genreText.value = movieInDetail.genres.joinToString(", ") { it.name }
            directorText.value =
                movieInDetail.production_companies.joinToString(", ") { it.name }
            sloganText.value = movieInDetail.tagline
        }
    }

    private fun checkFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            isFavorite.value = db.getDao().getMovieById(id) != null
        }
    }

    fun favorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (db.getDao().getMovieById(id) == null) {
                db.getDao().insertMovie(
                    MovieItemDb(
                        id,
                        reviewText.value,
                        titleText.value,
                        yearOfProductionText.value,
                        posterPath.value,
                        countryText.value,
                        genreText.value,
                        directorText.value,
                        sloganText.value
                    )
                )
                isFavorite.value = true
            } else {
                db.getDao().deleteMovieById(id)
                isFavorite.value = false
            }
        }
    }
}

private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}