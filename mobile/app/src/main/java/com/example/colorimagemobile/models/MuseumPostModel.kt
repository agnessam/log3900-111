package com.example.colorimagemobile.models

data class CommentInterface(
    val content: String,
    val authorId: UserModel.AllInfo,
    val postId: String,
    var createdAt: String?,
    val updatedAt: String?,
)

data class MuseumPostModel(
    val _id: String,
    val dataUri: String,
    val ownerId: String,
    val ownerModel: String,
    val name: String,
    val comments: ArrayList<CommentInterface>,
    val likes: ArrayList<String>
)
