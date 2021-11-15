package com.example.colorimagemobile.services.avatar

import com.example.colorimagemobile.models.AvatarModel

object AvatarService {
    private lateinit var allAvatar : ArrayList<AvatarModel.AllInfo>

    fun setAvatars(allAvatar:ArrayList<AvatarModel.AllInfo>){
        this.allAvatar  = allAvatar
    }
    fun getAvatars() : ArrayList<AvatarModel.AllInfo> {
        return this.allAvatar
    }
}