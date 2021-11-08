package com.example.colorimagemobile.classes

import com.google.gson.Gson
import org.json.JSONObject

class JSONConvertor {

    companion object {

        // converts an object to JSON object
        fun convertToJSON(data: Any): JSONObject {
            val gson = Gson()
            val jsonData = gson.toJson(data)
            return JSONObject(jsonData)
        }
    }
}