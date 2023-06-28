package com.example.filmsapp.fragments

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.filmsapp.R
import com.example.filmsapp.dataBase.MainDb
import com.example.filmsapp.dataBase.MovieItemDb
import java.util.concurrent.Executors
import com.example.filmsapp.databinding.FragmentMovieBinding
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieFragment : ViewBindingFragment<FragmentMovieBinding>() {
    private lateinit var db: MainDb
    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieBinding {
        return FragmentMovieBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getInt("movieId")
        db = MainDb.getDb(requireContext())
        if (movieId == 0) {
            initializeByParcelable()
        } else {
            initializeByDb(movieId!!)
        }

    }

    private fun favorite(id: Int, poster_path: String) {
        lifecycleScope.launch {
            val isFavorite = withContext(Dispatchers.IO) {
                db.getDao().getMovieById(id) != null
            }

            withContext(Dispatchers.Main) {
                if (isFavorite) {
                    binding.favorite.setBackgroundResource(R.drawable.icon_favorite_press)
                }
            }
        }

        binding.favorite.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val item = MovieItemDb(
                    id,
                    binding.reviewText.text.toString(),
                    binding.titleText.text.toString(),
                    binding.yearOfProductionText.text.toString(),
                    poster_path,
                    binding.countryText.text.toString(),
                    binding.genreText.text.toString(),
                    binding.directorText.text.toString(),
                    binding.sloganText.text.toString()
                )

                var isFavorite = true

                withContext(Dispatchers.IO) {
                    if (db.getDao().getMovieById(id) == null) {
                        db.getDao().insertMovie(item)
                        isFavorite = true
                    } else {
                        db.getDao().deleteMovieById(id)
                        isFavorite = false
                    }
                }

                if (isFavorite) {
                    binding.favorite.setBackgroundResource(R.drawable.icon_favorite_press)
                } else {
                    binding.favorite.setBackgroundResource(R.drawable.icon_favorite)
                }
            }
        }
    }

    private fun initializeByDb(id: Int) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val movieItemDb = db.getDao().getMovieById(id)
            val imageUrl = movieItemDb?.poster_path

            requireActivity().runOnUiThread {
                favorite(id, imageUrl!!)
                Glide.with(requireContext())
                    .load("${RetrofitUrls.IMAGE_URL}${imageUrl}")
                    .error(R.drawable.erorr)
                    .into(binding.imageMovie)
            }

            requireActivity().runOnUiThread {
                binding.yearOfProductionText.text = movieItemDb?.yearOfProduction
                binding.titleText.text = movieItemDb?.title
                binding.reviewText.text = movieItemDb?.overview
                binding.countryText.text = movieItemDb?.production_countries
                binding.genreText.text = movieItemDb?.genres
                binding.directorText.text = movieItemDb?.production_companies
                binding.sloganText.text = movieItemDb?.tagline
            }
        }
    }

    private fun initializeByParcelable() {
        val movie: MovieItem? = arguments?.parcelable("movie")
        binding.titleText.text = movie!!.title
        binding.reviewText.text = movie.overview

        favorite(movie.id, movie.poster_path)

        Glide.with(requireContext())
            .load("${RetrofitUrls.IMAGE_URL}${movie.poster_path}")
            .error(R.drawable.erorr)
            .into(binding.imageMovie)

        lifecycleScope.launch(Dispatchers.IO) {
            val movieInDetail: Movie = RetrofitParse().getMovieById(movie.id)

            withContext(Dispatchers.Main) {
                with(binding) {
                    yearOfProductionText.text = movieInDetail.release_date
                    countryText.text =
                        movieInDetail.production_countries.joinToString(", ") { it.name }
                    genreText.text = movieInDetail.genres.joinToString(", ") { it.name }
                    directorText.text =
                        movieInDetail.production_companies.joinToString(", ") { it.name }
                    sloganText.text = movieInDetail.tagline
                }
            }
        }
    }
}

private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
