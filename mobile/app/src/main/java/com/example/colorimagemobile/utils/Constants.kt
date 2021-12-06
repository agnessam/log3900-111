package com.example.colorimagemobile.utils

class Constants {

    // unspecific constants
    companion object {
        const val DEBUG_KEY = "PRIVATE"  // debug log key

        //global
        const val EMPTY_STRING =""
        const val GENERAL_CHANNEL_NAME = "General"
        const val CAMERA_REQUEST_CODE = 1
        const val PNG =".png"
        const val AVATAR_ALL_INFO_DEFAULT = true

        const val NB_DATA_ROWS = 3
        const val MINUTES = " minutes"
        const val NOTIFICATION_URI ="android.resource://com.example.colorimagemobile/raw/pop"
        const val MIN_LENGTH = 4
        const val MAX_LENGTH = 12
        const val PASSWORD_MIN_LENGTH = 8

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
            const val START_SELECTION_EVENT = "selection-start"
            const val CONFIRM_SELECTION_EVENT = "selection-confirm"
            const val TRANSFORM_SELECTION_EVENT = "selection-transform"
            const val UPDATE_DRAWING_NOTIFICATION = "drawing-update-notification"
            const val UPDATE_DRAWING_EVENT = "drawing-update"
            const val FETCH_DRAWING_NOTIFICATION = "drawing-fetch"
            const val DELETE_SELECTION_EVENT = "selection-delete"
            const val PRIMARY_COLOR_EVENT = "color-primary-update"
            const val SECONDARY_COLOR_EVENT = "color-secondary-update"
            const val LINE_WIDTH_EVENT = "line-width-update"
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
            const val AVATAR_PATH ="api/avatars"

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
            const val DEFAULT_WIDTH = 1860
            const val DEFAULT_HEIGHT = 1000

            const val MAX_OPACITY = 255
            const val PRIMARY_COLOR = "rgba(67, 118, 169, 255)" // blue-ish
            const val SECONDARY_COLOR = "rgba(237, 73, 86, 255)" // red-ish
        }
    }

    class TEAMS {
        companion object {
            const val CURRENT_TEAM_ID_KEY = "CurrentTeam"
        }
    }
    class USERS {
        companion object {
            const val CURRENT_USER_ID_KEY = "CurrentUser"
        }
    }


    class SEARCH {
        companion object {
            const val CURRENT_QUERY = "CurrentQueryObject"
        }
    }

    class DRAWINGS {
        companion object {
            const val CURRENT_DRAWING_ID_KEY = "CurrentDrawing"
        }
    }

    // sharedPreferences keys
    class STORAGE_KEY {
        companion object {
            const val MAIN = "localStorage"
            const val TOKEN = "token"
            const val PRIVACY_EMAIL = "searchByEmail"
            const val PRIVACY_FIRSTNAME = "searchByFirstname"
            const val PRIVACY_LASTNAME = "searchByLastname"
        }
    }

    data class SocketRoomInformation( val userId: String, val roomName: String)
}