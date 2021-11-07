package com.example.colorimagemobile.Shape

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout


class EditorView : RelativeLayout {
    private val TAG = "EditorView"

    private var mDrawingPreview: DrawingPreview? = null


    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        //Setup drawing view
        mDrawingPreview = DrawingPreview(context)
        val brushParam = setupDrawingView()


        //Add brush view
        addView(mDrawingPreview, brushParam)
    }


    private fun setupDrawingView(): LayoutParams? {
        mDrawingPreview!!.visibility = GONE

        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.addRule(CENTER_IN_PARENT, TRUE)

        return params
    }


    fun getDrawingView(): DrawingPreview? {
        return mDrawingPreview
    }


}
