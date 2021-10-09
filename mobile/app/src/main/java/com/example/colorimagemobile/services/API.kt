package com.example.colorimagemobile.services

import com.example.colorimagemobile.classes.LoginUser
import com.example.colorimagemobile.classes.RegisterNewUser
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.Constants.Companion.LOGIN_USER
import com.example.colorimagemobile.utils.Constants.Companion.LOGOUT_USER
import com.example.colorimagemobile.utils.Constants.Companion.REGISTER_USER
import retrofit2.Call
import retrofit2.http.*

interface API {

    @GET("api/v1")
    fun getUser(): Call<LoginUser>

    @Headers("Content-Type: application/json")
    @POST(LOGIN_USER)
    fun loginUser(@Body user: LoginUser): Call<HTTPResponseModel>

    @Headers("Content-Type: application/json")
    @POST(LOGOUT_USER)
    fun logoutUser(@Body user: LoginUser): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST(REGISTER_USER)
    fun registerUser(@Body newUser: RegisterNewUser): Call<HTTPResponseModel>
}