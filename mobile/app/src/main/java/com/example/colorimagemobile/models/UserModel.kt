package com.example.colorimagemobile.models

import com.google.gson.annotations.SerializedName

// class for different uses related to User
class UserModel {
    data class Login(val username: String, val password: String)
    data class Register(val firstName: String, val lastName: String, val username: String, val email: String, val password: String,val createdAt : String)
    data class Logout(val username: String)
    data class UpdateUser (var username: String,var description: String,var password: String, var avatar : AvatarModel.AllInfo)
    data class UpdateUserAvatar (var avatar : AvatarModel.AllInfo)

    // holds all the data of User
    data class AllInfo(
        @SerializedName("_id")
        val _id: String,

        @SerializedName("username")
        var username: String,

        @SerializedName("firstName")
        val firstName: String,

        @SerializedName("lastName")
        val lastName: String,

        @SerializedName("password")
        val password: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("description")
        var description: String,

        @SerializedName("teams")
        val teams: Array<String>,

        @SerializedName("drawings")
        val drawings: Array<String>,

        @SerializedName("avatar")
        val avatar : AvatarModel.AllInfo
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AllInfo

            if (!teams.contentEquals(other.teams)) return false
            if (!drawings.contentEquals(other.drawings)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = teams.contentHashCode()
            result = 31 * result + drawings.contentHashCode()
            return result
        }
    }
}



