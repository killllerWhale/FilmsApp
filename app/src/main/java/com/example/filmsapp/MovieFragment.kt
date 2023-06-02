package com.example.filmsapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.filmsapp.databinding.FragmentMovieBinding
import com.example.filmsapp.retrofit2.RetrofitParse
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.Movie
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.launch

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var movie: MovieItem? = null
        var movieInDetail: Movie? = null
        arguments?.let {
            movie = it.getParcelable("movie")
        }
        lifecycleScope.launch {
            val movieId = movie?.id ?: return@launch
            movieInDetail = RetrofitParse().getMovieById(movieId)
            with(binding) {
                titleText.text = movieInDetail!!.title
                yearOfProductionText.text = movieInDetail!!.release_date
                countryText.text = movieInDetail!!.production_countries.joinToString(", ") { it.name }
                genreText.text = movieInDetail!!.genres.joinToString(", ") { it.name }
                directorText.text = movieInDetail!!.production_companies.joinToString(", ") { it.name }
                reviewText.text = movieInDetail!!.overview
                sloganText.text = movieInDetail!!.tagline

                val imageLoader = ImageLoader(binding.root.context)
                val request = ImageRequest.Builder(binding.root.context)
                    .data("${RetrofitUrls.IMAGE_URL}${movieInDetail!!.poster_path}")
                    .target(imageMovie)
                    .build()
                imageLoader.enqueue(request)
            }
        }

    }

}