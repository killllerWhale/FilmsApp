package com.example.filmsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.example.filmsapp.databinding.ItemMovieBinding
import com.example.filmsapp.retrofit2.RetrofitUrls
import com.example.filmsapp.retrofit2.dataClases.MovieItem

class MoviesAdapter(
    private val onItemClick: (MovieItem) -> Unit,
    private val imageLoader: ImageLoader
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
            val request = ImageRequest.Builder(binding.root.context)
                .data("${RetrofitUrls.IMAGE_URL}${movie.poster_path}")
                .target(binding.cardImage)
                .build()

            imageLoader.enqueue(request)
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.title == newItem.title
        }
    }
}
