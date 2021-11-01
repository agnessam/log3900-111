package com.example.colorimagemobile.models

// class for different uses related to User
class UserModel {
    data class Login(val username: String, val password: String)
    data class Register(val firstName: String, val lastName: String, val username: String, val email: String, val password: String,val createdAt : String)
    data class Logout(val username: String)
    data class UpdateUser (var username: String,var description: String,var password: String)

    // holds all the data of User
    data class AllInfo(
        val _id: String,
        var username: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val email: String,
        var description: String,
    )
}



