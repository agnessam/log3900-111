package com.example.colorimagemobile.models

data class TeamIdModel(
    val _id: String,
    val name: String,
    var description: String,
    val owner: String,
    var members: ArrayList<UserModel.AllInfo>,
    val drawings: ArrayList<DrawingModel.Drawing>,
    val posts: ArrayList<PublishedMuseumPostModel>,
    var isPrivate: Boolean,
    var password: String?,
    val memberLimit: Int?)

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

data class UpdateTeam(
    val description: String,
    val isPrivate: Boolean,
    var password: String? = null
)
