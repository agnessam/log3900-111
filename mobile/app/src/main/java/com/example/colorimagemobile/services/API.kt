package com.example.colorimagemobile.services

import com.example.colorimagemobile.classes.User
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface API {

    @GET("api/v1")
    fun getUser(): Call<User.Login>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGIN_USER)
    fun loginUser(@Body user: User.Login): Call<HTTPResponseModel>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGOUT_USER)
    fun logoutUser(@Body user: User.Logout): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.REGISTER_USER)
    fun registerUser(@Body newUser: User.Register): Call<HTTPResponseModel>
}