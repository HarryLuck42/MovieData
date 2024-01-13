package com.corp.luqman.movielisting.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.data.models.Favorite
import com.corp.luqman.movielisting.databinding.ItemMovieBinding
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers

class FavoriteAdapter(val context: Context, val listMovie: MutableList<Favorite>, val clickListener: FavoriteListener ) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(movie: Favorite, clickListener: FavoriteListener){
            Glide.with(itemView.context).load(Const.imageUrlbase + movie.posterPath).placeholder(R.drawable.loading_animation)
                .apply(
                    RequestOptions()
                    .error(R.drawable.ic_broken))
                .into(binding.ivMovie)
            binding.titleMovie.text = movie.title
            binding.tvDateReleaseMovie.text = movie.releaseDate
            binding.btnDetailMovie.setOnClickListener {
                clickListener.onClick(movie)
            }
            binding.btnFavorite.setImageResource(R.drawable.fill_star)
            binding.btnSinopsis.setOnClickListener {
                Helpers.showGeneralOkDialog(
                    binding.root.context,
                    itemView.context.getString(R.string.sinopsis),
                    movie.overview!!
                )
            }
            binding.btnFavorite.setOnClickListener {
                clickListener.onFavorite(movie)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieBinding.inflate(layoutInflater)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieSelected = listMovie[position]
        holder.setData(movieSelected, clickListener)
    }
}

class FavoriteListener(val clickListener: (movie: Long) -> Unit, val clickFavorite: (movie: Favorite) -> Unit) {
    fun onClick(movie : Favorite) = clickListener(movie.id)

    fun onFavorite(movie: Favorite) = clickFavorite(movie)
}