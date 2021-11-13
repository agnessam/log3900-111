package com.example.colorimagemobile.models

// iterate over points and split to create Point() object
data class Polyline(val id: String, val name: String, val points: String, val style: String)

data class CustomSVG(
    val width: String,
    val height: String,
    val style: String,
//    val polyline: ArrayList<Polyline>? // example of parsing
)
