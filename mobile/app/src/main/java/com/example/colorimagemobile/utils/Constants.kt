package com.example.colorimagemobile.utils

class Constants {

    // unspecific constants
    companion object {
        const val DEBUG_KEY = "PRIVATE"  // debug log key

        // profile user log history
        const val LAST_LOGIN_DATE = "lastLogin"
        const val LAST_LOGOUT_DATE ="lastLogout"

        const val EMPTY_STRING =""
        const val GENERAL_CHANNEL_NAME = "General"

        const val NB_DATA_ROWS = 3
    }

    class SOCKETS {
        companion object {
            const val ROOM_EVENT_NAME = "room"
            const val LEAVE_ROOM_EVENT_NAME = "leaveRoom"

            const val CHAT_NAMESPACE_NAME = "chat"
            const val TEXT_MESSAGE_EVENT_NAME = "text"

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
            const val USER_PATH ="api/users/"
            const val TEXT_CHANNEL_PATH ="api/channels/"
            const val MESSAGES_PATH = "api/messages"
            const val USER = "api/users/"

            // drawings
            const val ALL_DRAWINGS = "api/drawings/"

            // teams
            const val TEAMS = "api/teams/"

            // search
            const val SEARCH = "api/search"

            // post
            const val MUSEUM_POST = "api/posts"
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

    class TEAMS {
        companion object {
            const val CURRENT_TEAM_ID_KEY = "CurrentTeam"
        }
    }

    class SEARCH {
        companion object {
            const val CURRENT_QUERY = "CurrentQueryObject"
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