package com.example.colorimagemobile.models

class User(username: String, password: String) {
    var username: String = ""
    var password: String = ""

    init {
        this.username = username
        this.password = password
    }
}