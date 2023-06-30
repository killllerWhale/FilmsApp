package com.example.filmsapp.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.filmsapp.R
import com.example.filmsapp.databinding.FragmentMovieBinding
import com.example.filmsapp.fragments.ViewBindingFragment
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieFragment : ViewBindingFragment<FragmentMovieBinding>() {

    private val vm by lazy {
        ViewModelProvider(this)[MovieVM::class.java]
    }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMovieBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.initializeMovie(arguments, requireContext())

        binding.favorite.setOnClickListener {
            vm.favorite(requireArguments().getInt("movieId"))
        }

        vm.isFavorite
            .onEach { if (it) binding.favorite.setBackgroundResource(R.drawable.icon_favorite_press) }
            .launchIn(lifecycleScope)

        combine(
            vm.titleText,
            vm.reviewText,
            vm.posterPath,
            vm.yearOfProductionText,
            vm.countryText,
            vm.genreText,
            vm.directorText,
            vm.sloganText
        ) { values ->
            with(binding) {
                titleText.text = values[0]
                reviewText.text = values[1]

                Glide.with(requireContext())
                    .load(values[2])
                    .error(R.drawable.erorr)
                    .into(binding.imageMovie)

                yearOfProductionText.text = values[3]
                countryText.text = values[4]
                genreText.text = values[5]
                directorText.text = values[6]
                sloganText.text = values[7]
            }
        }.launchIn(lifecycleScope)
    }
}