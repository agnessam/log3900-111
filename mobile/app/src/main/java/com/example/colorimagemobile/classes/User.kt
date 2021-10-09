package com.example.colorimagemobile.classes

// classes for different uses related to User

data class LoginUser(val username: String, val password: String)
data class RegisterNewUser(val firstName: String, val lastName: String, val username: String, val email: String, val password: String)