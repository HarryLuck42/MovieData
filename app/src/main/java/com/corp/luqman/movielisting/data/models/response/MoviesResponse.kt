package com.corp.luqman.movielisting.data.models.response

import com.corp.luqman.movielisting.data.models.Movie
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MoviesResponse (
    @SerializedName("page")
    var page : Int? = 0,
    @SerializedName("vote_count")
    var totalResults : Int? = 0,
    @SerializedName("total_results")
    var totalPages : Int? = 0,
    @SerializedName("results")
    var results: MutableList<Movie>?
): Serializable