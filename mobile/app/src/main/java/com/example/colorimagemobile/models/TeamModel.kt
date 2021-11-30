package com.example.colorimagemobile.models

data class TeamModel(
    val _id: String,
    val name: String,
    val description: String,
    val owner: String,
    var members: ArrayList<String>,
    val drawings: ArrayList<String>,
    val posts: ArrayList<String>,
    val memberLimit: Int?,
    val isPrivate: Boolean,
    val password: String?
)

data class CreateTeamModel(
    val name: String,
    val description: String,
    val isPrivate: Boolean,
    val memberLimit: Int?,
    val password: String? = null,
    val owner: String)