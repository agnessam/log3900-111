package com.example.colorimagemobile.models

import java.util.*
import kotlin.collections.ArrayList

data class CommentInterface(
    val content: String,
    val author: UserModel.AllInfo,
    val postId: String,
    var createdAt: String?,
    val updatedAt: String?,
)

data class MuseumPostModel(
    val _id: String,
    val dataUri: String,
    val owner: DrawingOwner,
    val ownerModel: String,
    val name: String,
    val comments: ArrayList<CommentInterface>,
    val likes: ArrayList<String>,
    var createdAt: Date?,
    val updatedAt: String?,
)

// Difference: comments is array of String rather than CommentInterface
data class PublishedMuseumPostModel(
    val _id: String,
    val dataUri: String,
    val owner: UserModel.AllInfo,
    val ownerModel: String,
    val name: String,
    val comments: ArrayList<String>,
    val likes: ArrayList<String>,
    var createdAt: String?,
    val updatedAt: String?,
)
