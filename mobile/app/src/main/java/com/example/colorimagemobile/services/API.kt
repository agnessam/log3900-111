package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface API {

    @GET("api/v1")
    fun getUser(): Call<UserModel.Login>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGIN_USER)
    fun loginUser(@Body user: UserModel.Login): Call<HTTPResponseModel.LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGOUT_USER)
    fun logoutUser(@Body user: UserModel.Logout): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.REGISTER_USER)
    fun registerUser(@Body newUser: UserModel.Register): Call<HTTPResponseModel.RegisterResponse>
}