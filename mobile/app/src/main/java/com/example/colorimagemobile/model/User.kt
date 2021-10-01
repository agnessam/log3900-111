package com.example.colorimagemobile.model

class User(username: String, password: String) {
    var username: String = ""
    var password: String = ""

    init {
        this.username = username
        this.password = password
    }
}

class LoginResponse(username: String, token: String, error: String) {
    var username: String = ""
    var token: String = ""
    var error: String = ""

    init {
        this.username = username
        this.token = token
        this.error = error
    }
}