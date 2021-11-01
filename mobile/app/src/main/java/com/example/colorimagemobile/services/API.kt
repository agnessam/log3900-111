package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface API {

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGIN_USER)
    fun loginUser(@Body user: UserModel.Login): Call<HTTPResponseModel.LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGOUT_USER)
    fun logoutUser(@Body user: UserModel.Logout): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.REGISTER_USER)
    fun registerUser(@Body newUser: UserModel.Register): Call<HTTPResponseModel.RegisterResponse>

    @GET(Constants.ENDPOINTS.GET_USER_BY_TOKEN)
    fun getUserByToken(@Header("Authorization") token: String): Call<HTTPResponseModel.GetUser>

    @Headers("Content-Type: application/json")
    @PATCH(Constants.ENDPOINTS.UPDATE_USER+"{id}")
    fun updateUser(@Header("Authorization")token: String, @Path ("id") id : String, @Body  newUser: UserModel.UpdateUser) : Call<HTTPResponseModel.UpdateUser>

}