package com.example.colorimagemobile.utils

class Constants {

    // unspecific constants
    companion object {
        const val DEBUG_KEY = "PRIVATE"  // debug log key

        // chat
        const val DEFAULT_ROOM_NAME = "default"
        const val CHAT_NAMESPACE_NAME = "chat"
        const val TEXT_MESSAGE_EVENT_NAME = "text"

        // profile user log history
        const val LAST_LOGIN_DATE = "lastLogin"
        const val LAST_LOGOUT_DATE ="lastLogout"
    }

    class SOCKETS {
        companion object {
            const val ROOM_EVENT_NAME = "room"
            const val LEAVE_ROOM_EVENT_NAME = "leaveRoom"
            const val COLLABORATIVE_DRAWING_NAMESPACE = "drawing"
            const val IN_PROGRESS_DRAWING_EVENT = "draw-in-progress"
            const val CONFIRM_DRAWING_EVENT = "draw-confirm"
        }
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
            const val USER = "api/users/"
            const val GET_USER_BY_ID = "api/users/"
            const val UPDATE_USER = "api/users/"
            const val DELETE_USER = "api/users/:id"

            // drawings
            const val ALL_DRAWINGS = "api/drawings/"
        }
    }

    class DRAWING {
        companion object {
            const val MIN_WIDTH = 10
            const val MAX_WIDTH = 2440

            const val MIN_HEIGHT = 10
            const val MAX_HEIGHT = 1215
        }
    }

    // sharedPreferences keys
    class STORAGE_KEY {
        companion object {
            const val MAIN = "localStorage"
            const val TOKEN = "token"
            const val DRAWING_ROOM_ID = "drawingRoomName"
        }
    }
}