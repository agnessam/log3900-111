package com.example.colorimagemobile.utils

class Constants {

    // unspecific constants
    companion object {
        const val DEBUG_KEY = "PRIVATE"  // debug log key

        // chat
        const val DEFAULT_ROOM_NAME = "default"
        const val CHAT_NAMESPACE_NAME = "chat"
        const val TEXT_MESSAGE_EVENT_NAME = "text"

        //global
        const val EMPTY_STRING =""

        // profile user log history
        const val LAST_LOGIN_DATE = "lastLogin"
        const val LAST_LOGOUT_DATE ="lastLogout"

        const val TEST_ID_CHANNEL ="6188d7820e5bad79805f9e8a"
        const val TEST_ID_USER = "6188c941f458df3e258d7b54"

    }

    class URL {
        companion object {
            const val SERVER = "http://10.0.2.2:3000"
//        const val SERVER = "http://colorimage.us-east-1.elasticbeanstalk.com"
        }
    }

    class ENDPOINTS {
        companion object {
            const val LOGIN_USER = "api/auth/login"
            const val LOGOUT_USER = "api/auth/logout"
            const val REGISTER_USER = "api/auth/register"
            const val GET_USER_BY_TOKEN = "api/users/me"
            const val USER_PATH ="api/users/"
            const val TEXT_CHANNEL_PATH ="api/channels/"
            const val MESSAGES_PATH = "api/messages"


        }
    }

    // sharedPreferences keys
    class STORAGE_KEY {
        companion object {
            const val MAIN = "localStorage"
            const val TOKEN = "token"
        }
    }

    class SOCKET_KEY{
        companion object{
             const val CHAT_NAMESPACE_NAME = "chat";
             const val ROOM_EVENT_NAME = "room";
             const val TEXT_MESSAGE_EVENT_NAME = "text";
             const val CONNECTION_EVENT_NAME = "connection";
             const val DISCONNECTION_EVENT_NAME = "disconnect";
             const val LEAVE_ROOM_EVENT_NAME = "leaveRoom";
        }
    }


}