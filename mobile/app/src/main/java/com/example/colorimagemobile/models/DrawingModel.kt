package com.example.colorimagemobile.models

class DrawingModel {
    data class Drawing(val _id: String?, val dataUri: String, val owner: String, val name: String, val ownerModel: String, val privacyLevel: String, val password: String, val createdAt: String?, val updatedAt: String?)
    data class CreateDrawing(val _id: String?, val dataUri: String, val owner: String, val ownerModel: String, val name: String, val privacyLevel: String, val password: String?)
    data class SaveDrawing(val dataUri: String)
}