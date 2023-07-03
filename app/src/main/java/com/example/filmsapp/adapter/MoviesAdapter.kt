package com.example.filmsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ItemMovieBinding
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.MovieItem

class MoviesAdapter(
    private val context: Context,
    private val onItemClick: (MovieItem) -> Unit,
) : PagingDataAdapter<MovieItem, MoviesAdapter.MovieViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = getItem(position)
                    if (movie != null) {
                        onItemClick(movie)
                    }
                }
            }
        }

        fun bind(movie: MovieItem) {
            binding.cardTitle.text = movie.title
            Glide.with(context)
                .load("${RetrofitUrls.IMAGE_URL}${movie.poster_path}")
                .error(R.drawable.erorr)
                .into(binding.cardImage)
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.title == newItem.title && oldItem.backdrop_path == newItem.backdrop_path
        }
    }
}
