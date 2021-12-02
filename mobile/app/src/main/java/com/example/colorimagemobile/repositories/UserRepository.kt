package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.users.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val httpClient = RetrofitInstance.HTTP

    fun getUserByToken(token: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        val userLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()

        httpClient.getUserByToken(token = "Bearer $token").enqueue(object : Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    userLiveData.value = DataWrapper(null, "Sorry, failed to get your profile!", true)
                    return
                }
                val body = response.body() as HTTPResponseModel.UserResponse
                if (!body.err.isNullOrEmpty()) {
                    userLiveData.value = DataWrapper(null, body.err, true)
                    return
                }
                // account successfully created
                userLiveData.value = DataWrapper(response.body(), null, false)
            }
            // duplicate username is coming through here
            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                userLiveData.value = DataWrapper(null, "Sorry, failed to get your profile!", true)
            }
        })

        return userLiveData
    }

    // update user profile data
    fun updateUserData(dataToUpdate:UserModel.UpdateUser ,token: String, id: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {

        val updateLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()

        httpClient.updateUser(token = "Bearer $token",id, dataToUpdate).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    updateLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // account successfully update
                updateLiveData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                updateLiveData.value = DataWrapper(null, "Failed to update user data!", true)
            }

        })

        return updateLiveData
    }

    // get user by id
    fun getUserById(token: String,id:String): MutableLiveData<DataWrapper<UserModel.AllInfoWithData>> {
        val userData: MutableLiveData<DataWrapper<UserModel.AllInfoWithData>> = MutableLiveData()

        httpClient.getUserById(token = "Bearer $token",id).enqueue(object : Callback<UserModel.AllInfoWithData> {
            override fun onResponse(call: Call<UserModel.AllInfoWithData>, response: Response<UserModel.AllInfoWithData>) {
                if (!response.isSuccessful) {
                    userData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                userData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<UserModel.AllInfoWithData>, t: Throwable) {
                userData.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return userData
    }

    // delete user by id
    fun deleteUserById(token: String, id: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {

        val deleteUserData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()
        httpClient.deleteUserById(token = "Bearer $token",id).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    deleteUserData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // account successfully delete
                deleteUserData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                deleteUserData.value = DataWrapper(null, "Failed to delete account!", true)
            }

        })

        return deleteUserData
    }

    // get all user
    fun getAllUser(token: String): MutableLiveData<DataWrapper<ArrayList<UserModel.AllInfo>>> {
        val AllUserData: MutableLiveData<DataWrapper<ArrayList<UserModel.AllInfo>>> = MutableLiveData()

        httpClient.getAllUser(token = "Bearer $token").enqueue(object : Callback<ArrayList<UserModel.AllInfo>> {
            override fun onResponse(call: Call<ArrayList<UserModel.AllInfo>>, response: Response<ArrayList<UserModel.AllInfo>>) {
                if (!response.isSuccessful) {
                    AllUserData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                AllUserData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<ArrayList<UserModel.AllInfo>>, t: Throwable) {
                AllUserData.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return AllUserData
    }

  // get user team
    fun getUserTeams(token: String, userId: String): MutableLiveData<DataWrapper<ArrayList<TeamModel>>> {
        val teamsLiveData: MutableLiveData<DataWrapper<ArrayList<TeamModel>>> = MutableLiveData()

        httpClient.getUserTeams(token = "Bearer $token", userId).enqueue(object: Callback<ArrayList<TeamModel>> {
            override fun onResponse(call: Call<ArrayList<TeamModel>>, response: Response<ArrayList<TeamModel>>) {
                if (!response.isSuccessful) {
                    teamsLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // account successfully update
                teamsLiveData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<ArrayList<TeamModel>>, t: Throwable) {
                teamsLiveData.value = DataWrapper(null, "Failed to fetch teams!", true)
            }
        })

        return teamsLiveData
    }

    // get user drawings
    fun getUserDrawings(id: String): MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> {
        val userDrawingsLiveData: MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> = MutableLiveData()

        httpClient.getUserDrawings(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<List<DrawingModel.Drawing>> {
            override fun onResponse(call: Call<List<DrawingModel.Drawing>>, response: Response<List<DrawingModel.Drawing>>) {
                if (!response.isSuccessful) {
                    userDrawingsLiveData.value = DataWrapper(null, "An error occurred while fetching user's drawings!", true)
                    return
                }
                userDrawingsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<List<DrawingModel.Drawing>>, t: Throwable) {
                userDrawingsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch user's drawings!", true)
            }
        })

        return userDrawingsLiveData
    }

    // get user posts
    fun getUserPosts(id: String): MutableLiveData<DataWrapper<List<PublishedMuseumPostModel>>> {
        val userPostsLiveData: MutableLiveData<DataWrapper<List<PublishedMuseumPostModel>>> = MutableLiveData()

        httpClient.getUserPosts(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<List<PublishedMuseumPostModel>> {
            override fun onResponse(call: Call<List<PublishedMuseumPostModel>>, response: Response<List<PublishedMuseumPostModel>>) {
                if (!response.isSuccessful) {
                    userPostsLiveData.value = DataWrapper(null, "An error occurred while fetching user's posts!", true)
                    return
                }
                userPostsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<List<PublishedMuseumPostModel>>, t: Throwable) {
                userPostsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch user's posts!", true)
            }
        })

        return userPostsLiveData
    }
    //follow user
    fun followUser(userId: String): MutableLiveData<DataWrapper<UserModel.AllInfo>> {
        val followUserLiveData: MutableLiveData<DataWrapper<UserModel.AllInfo>> = MutableLiveData()

        httpClient.followUser(token = "Bearer ${UserService.getToken()}", userId).enqueue(object : Callback<UserModel.AllInfo> {
            override fun onResponse(call: Call<UserModel.AllInfo>, response: Response<UserModel.AllInfo>) {
                if (!response.isSuccessful) {
                    followUserLiveData.value = DataWrapper(null, "An error occurred while following user!", true)
                    return
                }
                followUserLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<UserModel.AllInfo>, t: Throwable) {
                followUserLiveData.value = DataWrapper(null, "Sorry, failed to get follow user!", true)
            }
        })

        return followUserLiveData
    }

    //unfollow user
    fun unfollowUser(userId: String): MutableLiveData<DataWrapper<UserModel.AllInfo>> {
        val unfollowUserLiveData: MutableLiveData<DataWrapper<UserModel.AllInfo>> = MutableLiveData()

        httpClient.unfollowUser(token = "Bearer ${UserService.getToken()}", userId).enqueue(object : Callback<UserModel.AllInfo> {
            override fun onResponse(call: Call<UserModel.AllInfo>, response: Response<UserModel.AllInfo>) {
                if (!response.isSuccessful) {
                    unfollowUserLiveData.value = DataWrapper(null, "An error occurred while unfollowing user!", true)
                    return
                }
                unfollowUserLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<UserModel.AllInfo>, t: Throwable) {
                unfollowUserLiveData.value = DataWrapper(null, "Sorry, failed to get unfollow user!", true)
            }
        })

        return unfollowUserLiveData
    }
    // update user profile password
    fun updateUserPassword(newPassword: UserModel.PasswordUpdate): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {

        val updatePasswordLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()

        httpClient.updateUserPassword(token = "Bearer ${UserService.getToken()}",UserService.getUserInfo()._id, newPassword).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    updatePasswordLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // password successfully update
                updatePasswordLiveData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                updatePasswordLiveData.value = DataWrapper(null, "Failed to update password!", true)
            }

        })

        return updatePasswordLiveData
    }

    // get user by id
    fun getUserStatistics(): MutableLiveData<DataWrapper<UserModel.Statistics>> {
        val userData: MutableLiveData<DataWrapper<UserModel.Statistics>> = MutableLiveData()

        httpClient.getUserStatistics(token = "Bearer ${UserService.getToken()}",UserService.getUserInfo()._id).enqueue(object : Callback<UserModel.Statistics> {
            override fun onResponse(call: Call<UserModel.Statistics>, response: Response<UserModel.Statistics>) {
                if (!response.isSuccessful) {
                    userData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                userData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<UserModel.Statistics>, t: Throwable) {
                userData.value = DataWrapper(null, "Failed to get User Statistics!", true)
            }
        })

        return userData
    }

    // update privacy
    fun updateUserPrivacy(newSetting: Privacy.Setting): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {

        val updateSettingLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()

        httpClient.updateUserPrivacy(token = "Bearer ${UserService.getToken()}",UserService.getUserInfo()._id, newSetting).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    updateSettingLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // settings successfully update
                updateSettingLiveData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                updateSettingLiveData.value = DataWrapper(null, "Failed to update search privacy setting!", true)
            }

        })

        return updateSettingLiveData
    }
    fun getUserStatus(): MutableLiveData<DataWrapper<HashMap<String, UserModel.STATUS>>> {
        val status: MutableLiveData<DataWrapper<HashMap<String, UserModel.STATUS>>> = MutableLiveData()

        httpClient.getUserStatus(token = "Bearer ${UserService.getToken()}").enqueue(object : Callback<HashMap<String, UserModel.STATUS>> {
            override fun onResponse(call: Call<HashMap<String, UserModel.STATUS>>, response: Response<HashMap<String, UserModel.STATUS>>) {
                if (!response.isSuccessful) {
                    status.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                status.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<HashMap<String, UserModel.STATUS>>, t: Throwable) {
                status.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return status
    }
}