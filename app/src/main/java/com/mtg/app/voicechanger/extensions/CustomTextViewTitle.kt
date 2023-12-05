package com.mtg.app.voicechanger.extensions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextViewTitle : AppCompatTextView {
    private var color1: String = "#FFFF77"
    private var color2: String = "#9F9F19"

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private var isCreateShadow = true
    private var linearGradient: LinearGradient? = null

    override fun onDraw(canvas: Canvas?) {
        if (isCreateShadow) {
            isCreateShadow = false
            val rectText = Rect()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                paint.getTextBounds(text, 0, text.length, rectText)
            } else rectText.set(0, 0, 0, height)
            linearGradient =
                LinearGradient(0f, height / 2f - rectText.height() / 2f, 0f, height / 2 + rectText.height() / 2f, Color.parseColor(color1), Color.parseColor(color2), Shader.TileMode.MIRROR)
        }
        linearGradient?.run {
            paint.shader = this
            paint.style = Paint.Style.FILL
            super.onDraw(canvas)
        }


        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.shader = null

        paint.color = Color.GREEN
        super.onDraw(canvas)

    }

}