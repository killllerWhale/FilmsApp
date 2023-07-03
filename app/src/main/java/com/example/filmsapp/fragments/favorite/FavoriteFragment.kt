package com.example.filmsapp.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.adapter.MoviesDbAdapter
import com.example.filmsapp.databinding.FragmentFavoriteBinding
import com.example.filmsapp.fragments.ViewBindingFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FavoriteFragment : ViewBindingFragment<FragmentFavoriteBinding>() {

    private val vm by lazy {
        ViewModelProvider(this)[FavoriteVM::class.java]
    }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoriteBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.createDb(requireContext())

        val moviesDbAdapter = MoviesDbAdapter(requireContext()) { movie ->
            loadFragment(movie.id)
        }
        binding.recyclerViewFavorite.adapter = moviesDbAdapter

        vm.allMoviesFlow
            .onEach {
                moviesDbAdapter.submitData(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun loadFragment(id: Int) {
        val bundle = Bundle()
        bundle.putInt("movieId", id)
        findNavController().navigate(R.id.movieFragment2, bundle)
    }
}