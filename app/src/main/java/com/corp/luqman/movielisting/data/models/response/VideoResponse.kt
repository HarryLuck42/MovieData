package com.corp.luqman.movielisting.data.models.response

import com.corp.luqman.movielisting.data.models.Video
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoResponse (
    @SerializedName("id")
    var id : Long? = 0,
    @SerializedName("results")
    var results: MutableList<Video>?
): Serializable