package com.example.filmsapp.fragments

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.filmsapp.R
import com.example.filmsapp.dataBase.MainDb
import com.example.filmsapp.dataBase.MovieItemDb
import com.example.filmsapp.databinding.FragmentMovieBinding
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieFragment : ViewBindingFragment<FragmentMovieBinding>() {
    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieBinding {
        return FragmentMovieBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie: MovieItem? = arguments?.parcelable("movie")
        val db = MainDb.getDb(requireContext())

        binding.titleText.text = movie?.title
        binding.reviewText.text = movie?.overview

        Glide.with(requireContext())
            .load("${RetrofitUrls.IMAGE_URL}${movie?.poster_path}")
            .error(R.drawable.erorr)
            .into(binding.imageMovie)

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

        binding.favorite.setOnClickListener {
            Thread {
                db.getDao().insertMovie(
                    MovieItemDb(
                        movie!!.id,
                        movie.overview,
                        movie.title,
                        movie.poster_path,
                        binding.yearOfProductionText.text.toString(),
                        binding.countryText.text.toString(),
                        binding.genreText.text.toString(),
                        binding.directorText.text.toString(),
                        binding.sloganText.text.toString()
                    )
                )
            }.start()
        }
    }

    private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }
}