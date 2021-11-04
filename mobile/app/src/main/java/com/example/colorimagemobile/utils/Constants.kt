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
            const val GET_ALL_USER = "api/users/"
            const val GET_USER_BY_ID = "api/users/"
            const val UPDATE_USER = "api/users/"
            const val DELETE_USER = "api/users/:id"

            // drawings
            const val GET_ALL_DRAWINGS = "api/drawings"


        }
    }

    class DRAWING {
        companion object {
            const val MIN_WIDTH = 300
            const val MAX_WIDTH = 1500

            const val MIN_HEIGHT = 200
            const val MAX_HEIGHT = 1000
        }
    }

    // sharedPreferences keys
    class STORAGE_KEY {
        companion object {
            const val MAIN = "localStorage"
            const val TOKEN = "token"
        }
    }
}