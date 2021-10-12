package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.User
import com.example.colorimagemobile.utils.Constants.Companion.LOGIN_POST_USER
import com.example.colorimagemobile.utils.Constants.Companion.LOGOUT_POST_USER
import retrofit2.Call
import retrofit2.http.*

interface API {

    @GET("api/v1")
    fun getUser(): Call<User>

    @Headers("Content-Type: application/json")
    @POST(LOGIN_POST_USER)
    fun loginUser(@Body user: User): Call<HTTPResponseModel>

    @Headers("Content-Type: application/json")
    @POST(LOGOUT_POST_USER)
    fun logoutUser(@Body user: User): Call<Boolean>
}