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


class CustomViewRealGun : AppCompatTextView {
    private var color1: String = "#C6E9FF"
    private var color2: String = "#72C7FE"

    private val colors: List<Pair<String, String>> = listOf(
        "#2DA89B" to "#00FFE5",
        "#A88D2D" to "#FFB800",
        "#2DA848" to "#FFEE97",
        "#E88BFF" to "#7C4FFF",
        "#FF8B8B" to "#A52C2C",
        "#FFFFFF" to "#FFEE97",
        "#2DA89B" to "#00FFE5",
        "#A88D2D" to "#FFB800",
        "#2DA848" to "#FFEE97",
        "#E88BFF" to "#7C4FFF",
    )

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    //    private var color1 = Color.parseColor("#2DA89B")
//    private var color2 = Color.parseColor("#00FFE5")
    private var isCreateShadow = true
    private var linearGradient: LinearGradient? = null

    fun setGradientColors(position: Int) {
        val colorPair = getColorPairByPosition(position)
        color1 = colorPair.first
        color2 = colorPair.second
        isCreateShadow = true
        invalidate()
    }

    private fun getColorPairByPosition(position: Int): Pair<String, String> {
        val index = position % colors.size
        return colors[index]
    }

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