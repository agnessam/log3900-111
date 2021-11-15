package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.MessageModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface API {

    // login logout region
    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGIN_USER)
    fun loginUser(@Body user: UserModel.Login): Call<HTTPResponseModel.LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGOUT_USER)
    fun logoutUser(@Body user: UserModel.Logout): Call<Boolean>


    // register region
    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.REGISTER_USER)
    fun registerUser(@Body newUser: UserModel.Register): Call<HTTPResponseModel.RegisterResponse>


    // User crud region
    @GET(Constants.ENDPOINTS.GET_USER_BY_TOKEN)
    fun getUserByToken(@Header("Authorization") token: String): Call<HTTPResponseModel.UserResponse>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.USER_PATH)
    fun getAllUser(@Header("Authorization") token: String): Call<List<UserModel.AllInfo>>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun getUserById(@Header("Authorization") token: String, @Path ("id") id : String): Call<UserModel.AllInfo>

    @Headers("Content-Type: application/json")
    @PATCH(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun updateUser(@Header("Authorization")token: String, @Path ("id") id : String, @Body  newUser: UserModel.UpdateUser) : Call<HTTPResponseModel.UserResponse>


    @Headers("Content-Type: application/json")
    @PATCH(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun updateLogHistory(@Header("Authorization")token: String, @Path ("id") id : String, @Body  newLogHistory:  UserModel.UpdateLogHistory) : Call<HTTPResponseModel.UserResponse>

    @Headers("Content-Type: application/json")
    @DELETE(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun deleteUserById(@Header("Authorization")token: String, @Path ("id") id : String) : Call<HTTPResponseModel.UserResponse>

    //  TextChannel region
    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "{channelId}" + "/messages")
    fun getAllTextChannelMessages(@Header("Authorization") token: String, @Path ("channelId") channelId: String): Call<ArrayList<TextChannelModel.AllInfo>>

    //  TextChannel region
    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.TEXT_CHANNEL_PATH)
    fun getAllTextChannel(@Header("Authorization")token: String): Call<ArrayList<TextChannelModel.AllInfo>>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.TEXT_CHANNEL_PATH)
    fun addChannel(@Header("Authorization")token: String,@Body newUser: TextChannelModel.AllInfo): Call<TextChannelModel.AllInfo>

    @Headers("Content-Type: application/json")
    @DELETE(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "{channelId}")
    fun deleteChannelById(@Header("Authorization")token: String,@Path ("channelId") channelId: String): Call<TextChannelModel.AllInfo>

    // region message
    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.MESSAGES_PATH)
    fun getAllMessage(@Header("Authorization")token: String): Call<List<MessageModel.AllInfo>>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.MESSAGES_PATH)
    fun sendMessage(@Header("Authorization")token: String, @Body newMessage: MessageModel.SendMessage ): Call<MessageModel.AllInfo>

    @Headers("Content-Type: application/json")
    @DELETE(Constants.ENDPOINTS.MESSAGES_PATH+"/"+"{messageId}")
    fun deleteMessageById(@Header("Authorization")token: String,@Path ("messageId") messageId : String): Call<MessageModel.AllInfo>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.MESSAGES_PATH)
    fun storeMessage(@Header("Authorization")token: String, @Body newMessage: List<MessageModel.SendMessage> ): Call<List<MessageModel.AllInfo>>

    @GET(Constants.ENDPOINTS.ALL_DRAWINGS)
    fun getAllDrawings(@Header("Authorization") token: String): Call<List<DrawingModel.Drawing>>

    @GET("${Constants.ENDPOINTS.USER}{id}/teams")
    fun getUserTeams(@Header("Authorization") token: String, @Path ("id") id: String): Call<List<TeamModel>>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.ALL_DRAWINGS)
    fun createNewDrawing(@Header("Authorization") token: String, @Body drawing: DrawingModel.CreateDrawing): Call<DrawingModel.CreateDrawing>
}