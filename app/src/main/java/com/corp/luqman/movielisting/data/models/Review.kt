package com.corp.luqman.movielisting.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Review(
    @SerializedName("author")
    var author : String? = "",
    @SerializedName("content")
    var content : String? = "",
    @SerializedName("id")
    var id : String? = "",
    @SerializedName("url")
    var url : String? = ""
): Serializable