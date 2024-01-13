package com.corp.luqman.movielisting.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Countries (
    @SerializedName("iso_3166_1")
    var code : String? = "",
    @SerializedName("name")
    var name : String? = ""
): Serializable