package com.corp.luqman.movielisting.data.models.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ErrorResponse (
    @SerializedName("error")
    var error: String = "",
    @SerializedName("error_description")
    var error_description: String = "",
    @SerializedName("message")
    var message: String = ""
): Serializable