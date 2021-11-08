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

        // convert receving data into JSON object
        fun <T> getJSONObject(args: Array<*>, objClass: Class<T>): T {
            return Gson().fromJson(args[0].toString(), objClass)
        }
    }
}