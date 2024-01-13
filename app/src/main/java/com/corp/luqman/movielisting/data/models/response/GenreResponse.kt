package com.corp.luqman.movielisting.data.models.response

import com.corp.luqman.movielisting.data.models.GenreData
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class GenreResponse (
    @SerializedName("genres")
    var genres: MutableList<GenreData>?
): Serializable