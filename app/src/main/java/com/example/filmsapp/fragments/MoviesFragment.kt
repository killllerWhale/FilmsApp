package com.example.filmsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.adapter.MoviesAdapter
import com.example.filmsapp.databinding.FragmentMoviesBinding
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MoviesFragment : ViewBindingFragment<FragmentMoviesBinding>() {

    private val movieAdapter by lazy {
        MoviesAdapter(requireContext()) { movie ->
            loadFragment(movie)
        }
    }

    private lateinit var vm: MoviesVM

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMoviesBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this)[MoviesVM::class.java]

        if (!vm.isNetworkAvailable(requireContext())) {
            Toast.makeText(
                requireContext(),
                "Отсутствует подключение к интернету",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.recyclerView.adapter = movieAdapter
        searchViewCreated()

        vm.moviesFlow
            .onEach { movieAdapter.submitData(it) }
            .launchIn(lifecycleScope)
    }

    private fun searchViewCreated() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vm.searchMovies(query)
                movieAdapter.refresh()
                return true
            }

            override fun onQueryTextChange(newText: String) = false
        })
    }

    private fun loadFragment(movie: MovieItem) {
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        findNavController().navigate(R.id.movieFragment2, bundle)
    }
}