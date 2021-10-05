package com.example.colorimagemobile.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// model class for API Response
class LoginResponseModel {
    @SerializedName("username")
    @Expose
    val username: String? = null

    @SerializedName("token")
    @Expose
    val token: String? = null

    @SerializedName("error")
    @Expose
    val error: String? = null
}
