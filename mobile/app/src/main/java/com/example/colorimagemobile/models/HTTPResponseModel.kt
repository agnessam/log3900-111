package com.example.colorimagemobile.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// model class for API Response
class HTTPResponseModel {
    data class LoginResponse(val user: UserModel.AllInfo, val token: String, val info: String, val error: String)
    data class RegisterResponse(val user: UserModel.AllInfo, val token: String, val info: String, val error: String)
}
