package com.example.filmsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.adapter.MoviesDbAdapter
import com.example.filmsapp.dataBase.MainDb
import com.example.filmsapp.dataBase.MovieItemDb
import com.example.filmsapp.databinding.FragmentFavoriteBinding


class FavoriteFragment : ViewBindingFragment<FragmentFavoriteBinding>() {
    private lateinit var moviesDbAdapter: MoviesDbAdapter

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = MainDb.getDb(requireContext())

        moviesDbAdapter = MoviesDbAdapter(requireContext()) { movie ->
            loadFragment(movie)
        }

        binding.recyclerViewFavorite.adapter = moviesDbAdapter
        db.getDao().getAllMovie().asLiveData().observe(requireActivity()){
            moviesDbAdapter.submitData(it)
        }
    }

    private fun loadFragment(movie: MovieItemDb) {
//        val bundle = Bundle()
//        bundle.putParcelable("movie", movie)
        findNavController().navigate(R.id.movieFragment2)
    }
}