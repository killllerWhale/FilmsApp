package com.example.filmsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.adapter.MoviesAdapter
import com.example.filmsapp.databinding.FragmentMoviesBinding
import com.example.filmsapp.retrofit2.dataClases.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MoviesFragment : ViewBindingFragment<FragmentMoviesBinding>() {
    private lateinit var vm: MoviesVM

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMoviesBinding.inflate(inflater)

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this)[MoviesVM::class.java]
        vm.isNetworkAvailable(requireContext())

        val movieAdapter = MoviesAdapter(requireContext()) { movie ->
            loadFragment(movie)
        }

        binding.recyclerView.adapter = movieAdapter

        vm.isNetworkAvailableBoolean.observe(viewLifecycleOwner) {
            if (it) {
                vm.moviesFlowResult.observe(viewLifecycleOwner) { pagingData ->
                    CoroutineScope(Dispatchers.Main).launch {
                        movieAdapter.submitData(pagingData)
                    }
                }
                searchViewCreated()
            } else {
                // Интернет недоступен
                Toast.makeText(
                    requireContext(),
                    "Отсутствует подключение к интернету",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun searchViewCreated() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vm.searchMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun loadFragment(movie: MovieItem) {
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        findNavController().navigate(R.id.movieFragment2, bundle)
    }
}