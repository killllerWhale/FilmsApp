package com.example.filmsapp.fragments.movie

import android.animation.Animator
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
            vm.favorite()
            if (!vm.isFavorite.value) {
                binding.favorite.visibility = View.INVISIBLE
                with(binding.lottieAnimationView) {
                    visibility = View.VISIBLE
                    playAnimation()
                    addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}

                        override fun onAnimationEnd(animation: Animator) {
                            visibility = View.GONE
                            binding.favorite.visibility = View.VISIBLE
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                }
            }
        }

        vm.isFavorite
            .onEach {
                if (it) binding.favorite.setBackgroundResource(R.drawable.icon_favorite_press)
                else binding.favorite.setBackgroundResource(R.drawable.icon_favorite)
            }
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