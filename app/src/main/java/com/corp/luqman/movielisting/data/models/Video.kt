package com.corp.luqman.movielisting.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Video (
    @SerializedName("id")
    var id : String? = "",
    @SerializedName("iso_639_1")
    var language : String? = "",
    @SerializedName("iso_3166_1")
    var country : String? = "",
    @SerializedName("key")
    var key : String? = "",
    @SerializedName("name")
    var name : String? = "",
    @SerializedName("site")
    var site : String? = "",
    @SerializedName("size")
    var size : Int? = 0,
    @SerializedName("type")
    var type : String? = ""

): Serializable