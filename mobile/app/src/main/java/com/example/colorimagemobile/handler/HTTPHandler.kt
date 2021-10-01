package com.example.colorimagemobile.handler

import com.example.colorimagemobile.model.LoginResponse
import com.example.colorimagemobile.model.User
import com.example.colorimagemobile.utils.Constants.Companion.LOGIN_POST_USER
import com.example.colorimagemobile.utils.Constants.Companion.LOGOUT_POST_USER
import retrofit2.Call
import retrofit2.http.*

interface HTTPHandler {

    @GET("api/v1")
    fun getUser(): Call<User>

    @Headers("Content-Type: application/json")
    @POST(LOGIN_POST_USER)
    fun loginUser(@Body user: User): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(LOGOUT_POST_USER)
    fun logoutUser(@Body user: User): Call<Boolean>
}