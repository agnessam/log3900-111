package com.example.colorimagemobile.services

import com.example.colorimagemobile.utils.Constants.Companion.SERVER_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// object -> singleton
object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val HTTP: API by lazy {
        retrofit.create(API::class.java)
    }
}