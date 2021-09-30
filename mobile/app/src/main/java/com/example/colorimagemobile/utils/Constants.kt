package com.example.colorimagemobile.utils

class Constants {

    companion object {
        const val SERVER_URL = "http://10.0.2.2:3000/"

        // routes
        const val LOGIN_POST_USER = "api/v1/login"
        const val LOGOUT_POST_USER = "api/v1/logout"

        // sharedPreferences keys
        const val LOCAL_STORAGE_KEY = "localStorage"
        const val SHARED_USERNAME_KEY = "username"
        const val SHARED_TOKEN_KEY = "token"

        // debug log key
        const val DEBUG_KEY = "DEBUG"

        // chat
        const val DEFAULT_ROOM_NAME = "default"
        const val CHAT_NAMESPACE_NAME = "chat"
        const val TEXT_MESSAGE_EVENT_NAME = "text message"
    }
}