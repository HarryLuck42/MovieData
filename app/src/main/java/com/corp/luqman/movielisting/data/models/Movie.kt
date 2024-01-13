package com.corp.luqman.movielisting.data.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Movie (
    @SerializedName("id")
    var id : Long = 0,

    @SerializedName("popularity")
    var popularity : Double? = 0.0,

    @SerializedName("vote_count")
    var voteCount : Int? = 0,

    @SerializedName("video")
    var video : Boolean? = false,

    @SerializedName("poster_path")
    var posterPath : String? = "",

    @SerializedName("adult")
    var adult : Boolean? = false,

    @SerializedName("backdrop_path")
    var backdropPath : String? = "",

    @SerializedName("original_language")
    var originalLanguage : String? = "",

    @SerializedName("original_title")
    var originalTitle : String? = "",

    @SerializedName("genre_ids")
    var genreIds : MutableList<Int>? = null,

    @SerializedName("title")
    var title : String? = "",

    @SerializedName("vote_average")
    var voteAverage : Double? = 0.0,

    @SerializedName("overview")
    var overview : String? = "",

    @SerializedName("release_date")
    var releaseDate : String? = "",

    var isFavorite: Boolean = false
): Serializable{
    fun convert(): Favorite {
        return Favorite(
            this.id,
            this.popularity,
            this.voteCount,
            this.video,
            this.posterPath,
            this.adult,
            this.backdropPath,
            this.originalLanguage,
            this.originalTitle,
            this.genreIds,
            this.title,
            this.voteAverage,
            this.overview,
            this.releaseDate
        )
    }
}