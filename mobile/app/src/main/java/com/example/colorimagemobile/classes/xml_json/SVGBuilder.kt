package com.example.colorimagemobile.classes.xml_json

import com.github.underscore.lodash.U

class SVGBuilder {

    private val parentBuilder: U.Builder = U.objectBuilder(); // root SVG element
    private val innerBuilder: U.Builder = U.objectBuilder(); // SVG inner styles

    init {
        addDefaultAttributes()
    }

    // add default svg attributes
    private fun addDefaultAttributes() {
        addAttr("xmlns", "http://www.w3.org/2000/svg")
    }

    // add all attributes to SVG (parent) element
    fun completeBuild() {
        parentBuilder.add("svg", innerBuilder)
    }

    // "-" to insert the value inside the svg itself instead of its child
    fun addAttr(key: String, value: String) {
        innerBuilder.add("-$key", value)
    }

    fun addAttr(key: String, value: Int) {
        addAttr(key, value.toString())
    }

    fun addAttr(key: String, value: Float) {
        addAttr(key, value.toString())
    }

    fun getJSON(): String {
        completeBuild()
        return parentBuilder.toJson()
    }

    fun getXML(): String {
        completeBuild()
        return parentBuilder.toXml()
    }

}