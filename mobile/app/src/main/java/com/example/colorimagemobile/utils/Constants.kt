package com.example.colorimagemobile.utils

class Constants {

    companion object {
        const val SERVER_URL = "http://10.0.2.2:3000"
//        const val SERVER_URL = "http://colorimage.us-east-1.elasticbeanstalk.com"

        // endpoints
        const val LOGIN_USER = "api/users/login"
        const val LOGOUT_USER = "api/users/logout"
        const val REGISTER_USER = "api/users/register"

        // sharedPreferences keys
        const val LOCAL_STORAGE_KEY = "localStorage"
        const val SHARED_USERNAME_KEY = "username"
        const val SHARED_TOKEN_KEY = "token"

        // debug log key
        const val DEBUG_KEY = "DEBUG"

        // chat
        const val DEFAULT_ROOM_NAME = "default"
        const val CHAT_NAMESPACE_NAME = "chat"
        const val TEXT_MESSAGE_EVENT_NAME = "text"
    }
}