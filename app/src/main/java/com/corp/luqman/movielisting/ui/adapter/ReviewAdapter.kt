package com.corp.luqman.movielisting.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corp.luqman.movielisting.data.models.Review
import com.corp.luqman.movielisting.databinding.ItemReviewMovieBinding

class ReviewAdapter(val context: Context, val listReview: MutableList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemReviewMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(review: Review){
            binding.nameReviewer.text = review.author
            binding.descReview.text = review.content
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemReviewMovieBinding.inflate(layoutInflater)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieSelected = listReview[position]
        holder.setData(movieSelected)
    }

}
