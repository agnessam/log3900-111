package com.example.colorimagemobile.models

import java.util.*

class DrawingModel {
    data class Drawing(val _id: String?, val dataUri: String, val ownerId: String, val name: String, val ownerModel: String, val privacyLevel: String, val password: String, val createdAt: Date, val updatedAt: Date)
    data class CreateDrawing(val _id: String?, val dataUri: String, val ownerId: String, val ownerModel: String, val name: String)
}