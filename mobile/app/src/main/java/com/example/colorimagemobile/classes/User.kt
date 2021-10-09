package com.example.colorimagemobile.classes

class User(username: String, password: String) {
    var username: String = ""
    var password: String = ""

    init {
        this.username = username
        this.password = password
    }
}