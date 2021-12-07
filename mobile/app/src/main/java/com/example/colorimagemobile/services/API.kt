package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.utils.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST

import retrofit2.http.Multipart

interface API {

    // login logout region
    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.LOGIN_USER)
    fun loginUser(@Body user: UserModel.Login): Call<HTTPResponseModel.LoginResponse>

    @POST(Constants.ENDPOINTS.LOGOUT_USER)
    fun logoutUser(@Header("Authorization") token: String,@Body userId: UserModel.Logout): Call<Boolean>

    // register region
    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.REGISTER_USER)
    fun registerUser(@Body newUser: UserModel.Register): Call<HTTPResponseModel.RegisterResponse>

    // User crud region
    @GET(Constants.ENDPOINTS.GET_USER_BY_TOKEN)
    fun getUserByToken(@Header("Authorization") token: String): Call<HTTPResponseModel.UserResponse>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.USER_PATH)
    fun getAllUser(@Header("Authorization") token: String): Call<ArrayList<UserModel.AllInfo>>

    @Headers("Content-Type: application/json")
    @GET("${Constants.ENDPOINTS.USER_PATH}status")
    fun getUserStatus(@Header("Authorization") token: String): Call<HashMap<String, UserModel.STATUS>>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.USER_PATH+"{id}/statistics")
    fun getUserStatistics(@Header("Authorization") token: String, @Path ("id") id : String): Call<UserModel.Statistics>

    @PATCH(Constants.ENDPOINTS.USER_PATH+"{id}/changePassword")
    fun updateUserPassword(@Header("Authorization")token: String, @Path ("id") id : String, @Body  newPassword: UserModel.PasswordUpdate) : Call<HTTPResponseModel.UserResponse>

    @Headers("Content-Type: application/json")
    @PATCH(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun updateUserPrivacy(@Header("Authorization")token: String, @Path ("id") id : String, @Body  newUserSetting: UserModel.updatePrivacy) : Call<HTTPResponseModel.UserResponse>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun getUserById(@Header("Authorization") token: String, @Path ("id") id : String): Call<UserModel.AllInfoWithData>

    @Headers("Content-Type: application/json")
    @PATCH(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun updateUser(@Header("Authorization")token: String, @Path ("id") id : String, @Body  newUser: UserModel.UpdateUser) : Call<HTTPResponseModel.UserResponse>

    @Headers("Content-Type: application/json")
    @DELETE(Constants.ENDPOINTS.USER_PATH+"{id}")
    fun deleteUserById(@Header("Authorization")token: String, @Path ("id") id : String) : Call<HTTPResponseModel.UserResponse>

    @GET("${Constants.ENDPOINTS.USER_PATH}{id}/drawings")
    fun getUserDrawings(@Header("Authorization") token: String, @Path ("id") id: String): Call<List<DrawingModel.Drawing>>

    @GET("${Constants.ENDPOINTS.USER_PATH}{id}/posts")
    fun getUserPosts(@Header("Authorization") token: String, @Path ("id") id: String): Call<List<PublishedMuseumPostModel>>
    
    @Headers("Content-Type: application/json")
    @POST("${Constants.ENDPOINTS.USER_PATH}{id}/followers/follow")
    fun followUser(@Header("Authorization") token: String, @Path ("id") id: String): Call<UserModel.AllInfo>

    @Headers("Content-Type: application/json")
    @POST("${Constants.ENDPOINTS.USER_PATH}{id}/followers/unfollow")
    fun unfollowUser(@Header("Authorization") token: String, @Path ("id") id: String): Call<UserModel.AllInfo>

    //  TextChannel region
    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "{channelId}" + "/messages")
    fun getAllTextChannelMessages(@Header("Authorization") token: String, @Path ("channelId") channelId: String): Call<ArrayList<ChatSocketModel>>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "teams")
    fun getTeamChannels(@Header("Authorization") token: String): Call<ArrayList<TextChannelModel.AllInfo>>

    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "drawings/{id}")
    fun getChannelByDrawingId(@Header("Authorization") token: String, @Path ("id") id: String): Call<TextChannelModel.AllInfo>

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

    @GET(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "all/search")
    fun searchChannels(@Header("Authorization") token: String, @Query ("q") query: String): Call<ArrayList<TextChannelModel.AllInfo>>

    @Headers("Content-Type: application/json")
    @DELETE(Constants.ENDPOINTS.TEXT_CHANNEL_PATH + "{channelId}/messages")
    fun deleteMessages(@Header("Authorization")token: String,@Path ("channelId") channelId: String): Call<Any>

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
    fun getAllDrawings(@Header("Authorization") token: String): Call<ArrayList<DrawingModel.Drawing>>

    @GET("${Constants.ENDPOINTS.ALL_DRAWINGS}{id}")
    fun getDrawing(@Header("Authorization") token: String, @Path ("id") id: String): Call<DrawingModel.Drawing>

    @GET("${Constants.ENDPOINTS.USER_PATH}{id}/teams")
    fun getUserTeams(@Header("Authorization") token: String, @Path ("id") id: String): Call<ArrayList<TeamModel>>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.ALL_DRAWINGS)
    fun createNewDrawing(@Header("Authorization") token: String, @Body drawing: DrawingModel.CreateDrawing): Call<DrawingModel.Drawing>

    @Headers("Content-Type: application/json")
    @PATCH("${Constants.ENDPOINTS.ALL_DRAWINGS}{id}")
    fun updateDrawing(@Header("Authorization") token: String, @Path ("id") id: String, @Body drawing: DrawingModel.UpdateDrawing): Call<Any>

    @DELETE("${Constants.ENDPOINTS.ALL_DRAWINGS}{id}")
    fun deleteDrawing(@Header("Authorization") token: String, @Path ("id") id: String): Call<Any>

    @Headers("Content-Type: application/json")
    @POST("${Constants.ENDPOINTS.ALL_DRAWINGS}{id}/publish")
    fun publishDrawing(@Header("Authorization") token: String, @Path ("id") id: String, @Body drawing: DrawingModel.Drawing): Call<Any>
    
    @PATCH("${Constants.ENDPOINTS.ALL_DRAWINGS}{id}")
    fun saveDrawing(@Header("Authorization") token: String, @Path ("id") id: String, @Body drawing: DrawingModel.SaveDrawing): Call<DrawingModel.CreateDrawing>

    // region avatar
    @Headers("Content-Type: application/json")
    @GET(Constants.ENDPOINTS.AVATAR_PATH+"/default")
    fun getAllAvatar(@Header("Authorization") token: String): Call<ArrayList<AvatarModel.AllInfo>>

    @Multipart
    @POST(Constants.ENDPOINTS.AVATAR_PATH+"/upload")
    fun uploadAvatar(@Header("Authorization") token: String, @Part filePart: MultipartBody.Part?): Call<AvatarModel.AllInfo>

    // teams
    @GET("${Constants.ENDPOINTS.TEAMS}{id}/drawings")
    fun getTeamDrawings(@Header("Authorization") token: String, @Path ("id") id: String): Call<List<DrawingModel.Drawing>>

    @GET("${Constants.ENDPOINTS.TEAMS}{id}")
    fun getTeamById(@Header("Authorization") token: String, @Path ("id") id: String): Call<TeamIdModel>

    @GET("${Constants.ENDPOINTS.TEAMS}{id}/posts")
    fun getTeamPosts(@Header("Authorization") token: String, @Path ("id") id: String): Call<List<PublishedMuseumPostModel>>

    @GET(Constants.ENDPOINTS.TEAMS)
    fun getAllTeams(@Header("Authorization") token: String): Call<ArrayList<TeamModel>>

    @GET("${Constants.ENDPOINTS.TEAMS}protected")
    fun getProtectedTeams(@Header("Authorization") token: String): Call<ArrayList<TeamModel>>

    @Headers("Content-Type: application/json")
    @POST("${Constants.ENDPOINTS.TEAMS}{id}/join")
    fun joinTeam(@Header("Authorization") token: String, @Path ("id") id: String): Call<TeamModel>

    @Headers("Content-Type: application/json")
    @POST("${Constants.ENDPOINTS.TEAMS}{id}/leave")
    fun leaveTeam(@Header("Authorization") token: String, @Path ("id") id: String): Call<TeamModel>

    @Headers("Content-Type: application/json")
    @POST(Constants.ENDPOINTS.TEAMS)
    fun createNewTeam(@Header("Authorization") token: String, @Body team: CreateTeamModel): Call<TeamModel>

    @DELETE("${Constants.ENDPOINTS.TEAMS}{id}")
    fun deleteTeam(@Header("Authorization") token: String, @Path ("id") id: String): Call<Any>

    @PATCH("${Constants.ENDPOINTS.TEAMS}{id}")
    fun updateTeam(@Header("Authorization") token: String, @Path ("id") id: String, @Body team: UpdateTeam): Call<Any>

    // Search
    @GET(Constants.ENDPOINTS.SEARCH)
    fun getSearchQuery(@Header("Authorization") token: String, @Query ("q") query: String): Call<SearchModel>

    // Museum Posts
    @GET(Constants.ENDPOINTS.MUSEUM_POST)
    fun getAllPosts(@Header("Authorization") token: String): Call<ArrayList<MuseumPostModel>>

    @GET("${Constants.ENDPOINTS.MUSEUM_POST}/{id}")
    fun getPostById(@Header("Authorization") token: String, @Path ("id") postId: String): Call<MuseumPostModel>

    @Headers("Content-Type: application/json")
    @POST("${Constants.ENDPOINTS.MUSEUM_POST}/{id}/comments")
    fun postComment(@Header("Authorization") token: String, @Path ("id") postId: String, @Body comment: CommentInterface): Call<CommentInterface>

    @POST("${Constants.ENDPOINTS.MUSEUM_POST}/{id}/likes")
    fun likePost(@Header("Authorization") token: String, @Path ("id") id: String): Call<Any>

    @DELETE("${Constants.ENDPOINTS.MUSEUM_POST}/{id}/likes")
    fun unlikePost(@Header("Authorization") token: String, @Path ("id") id: String): Call<Any>
}