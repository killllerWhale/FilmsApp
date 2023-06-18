package com.example.filmsapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmsapp.R
import com.example.filmsapp.dataBase.MovieItemDb
import com.example.filmsapp.databinding.ItemMovieBinding
import com.example.filmsapp.retrofit2.RetrofitUrls

class MoviesDbAdapter(
    private val context: Context,
    private val onItemClick: (MovieItemDb) -> Unit,
) : RecyclerView.Adapter<MoviesDbAdapter.MovieViewHolder>() {

    private var movies: List<MovieItemDb> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(movies: List<MovieItemDb>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movies[position]
                    onItemClick(movie)
                }
            }
        }

        fun bind(movie: MovieItemDb) {
            binding.cardTitle.text = movie.title
            Glide.with(context)
                .load("${RetrofitUrls.IMAGE_URL}${movie.poster_path}")
                .error(R.drawable.erorr)
                .into(binding.cardImage)
        }
    }
}
