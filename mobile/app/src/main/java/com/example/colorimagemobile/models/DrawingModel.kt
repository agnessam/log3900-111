package com.example.colorimagemobile.models

import java.util.*

class DrawingModel {
    data class AllDrawings(val _id: String, val dataUri: String, val ownerId: String, val ownerModel: String, val createdAt: Date, val updatedAt: Date)
}