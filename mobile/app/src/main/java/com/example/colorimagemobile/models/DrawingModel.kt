package com.example.colorimagemobile.models

enum class PrivacyLevel {
    PUBLIC {
        override fun toString(): String = "public"
    },
    PRIVATE {
        override fun toString(): String = "private"
    },
    PROTECTED {
        override fun toString(): String = "protected"
    }
}

enum class OwnerModel {
    USER {
        override fun toString(): String = "User"
    },
    TEAM {
        override fun toString(): String = "Team"
    }
}

class DrawingModel {
    data class Drawing(val _id: String?, val dataUri: String, val owner: String, val name: String, val ownerModel: String, val privacyLevel: String, val password: String, val createdAt: String?, val updatedAt: String?)
    data class CreateDrawing(val _id: String?, val dataUri: String, val owner: String, val ownerModel: String, val name: String, val privacyLevel: String, val password: String?)
    data class SaveDrawing(val dataUri: String)
}