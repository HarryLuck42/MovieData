package com.corp.luqman.movielisting.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LanguageValue(
    @SerializedName("iso_639_1")
    var code : String? = "",
    @SerializedName("name")
    var name : String? = ""
): Serializable