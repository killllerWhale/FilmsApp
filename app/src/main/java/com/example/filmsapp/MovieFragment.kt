package com.example.filmsapp

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.request.CachePolicy
import com.example.filmsapp.databinding.FragmentMovieBinding
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieFragment : ViewBindingFragment<FragmentMovieBinding>() {
    override lateinit var binding: FragmentMovieBinding

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieBinding {
        binding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie: MovieItem? = arguments?.parcelable("movie")
        binding.titleText.text = movie?.title
        binding.reviewText.text = movie?.overview
        binding.imageMovie.load("${RetrofitUrls.IMAGE_URL}${movie?.poster_path}") {
            memoryCachePolicy(CachePolicy.ENABLED)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val movieInDetail: Movie = RetrofitParse().getMovieById(movie?.id ?: 1)

            with(binding) {
                yearOfProductionText.text = movieInDetail.release_date
                countryText.text = movieInDetail.production_countries.joinToString(", ") { it.name }
                genreText.text = movieInDetail.genres.joinToString(", ") { it.name }
                directorText.text =
                    movieInDetail.production_companies.joinToString(", ") { it.name }
                sloganText.text = movieInDetail.tagline

            }
        }
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }
}