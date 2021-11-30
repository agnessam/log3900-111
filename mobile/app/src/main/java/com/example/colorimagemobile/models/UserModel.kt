package com.example.colorimagemobile.models

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

// class for different uses related to User
class UserModel {
    data class Login(val username: String, val password: String)
    data class Register(val firstName: String, val lastName: String, val username: String, val email: String, val password: String,val createdAt : String)
    data class Logout(val username: String)
    data class UpdateUser (
        @SerializedName("username")
        var username: String?,

        @SerializedName("description")
        var description: String?,

        @SerializedName("avatar")
        var avatar : AvatarModel.AllInfo?
        )

    // holds all the data of User
    data class AllInfo(
        val _id: String,
        var username: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val email: String,
        var description: String,
        val teams: ArrayList<String>,
        val drawings: ArrayList<String>,
        var avatar : AvatarModel.AllInfo,
        val posts: ArrayList<String>,
        var followers: ArrayList<String>,
        var following: ArrayList<String>,
        var lastLogin: Date?,
        var lastLogout: Date?,
        var collaborationHistory: ArrayList<Any>?,
    )
}



