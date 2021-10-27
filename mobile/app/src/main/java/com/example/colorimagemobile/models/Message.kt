<<<<<<< HEAD:mobile/app/src/main/java/com/example/colorimagemobile/models/Message.kt
package com.example.colorimagemobile.models
=======
package com.example.colorimagemobile.classes
>>>>>>> main:mobile/app/src/main/java/com/example/colorimagemobile/classes/Message.kt

class Message(message: String, timestamp: String, author: String, roomName: String) {
    var message: String = ""
    var timestamp: String = ""
    var author: String = ""
    var roomName: String = ""

    init {
        this.message = message
        this.timestamp = timestamp
        this.author = author
        this.roomName = roomName
    }
}