package com.example.colorimagemobile.services.avatar

import com.example.colorimagemobile.models.AvatarModel

object AvatarService {
    private lateinit var allAvatar : ArrayList<AvatarModel.AllInfo>
    private lateinit var currentAvatar : AvatarModel.AllInfo

    fun setAvatars(allAvatar:ArrayList<AvatarModel.AllInfo>){
        this.allAvatar  = allAvatar

    }
    fun getAvatars() : ArrayList<AvatarModel.AllInfo> {
        return this.allAvatar
    }

    fun setCurrentAvatar(ChosenAvatar : AvatarModel.AllInfo){
       this.currentAvatar = ChosenAvatar
    }

    fun getCurrentAvatar (): AvatarModel.AllInfo{
        return this.currentAvatar
    }

}