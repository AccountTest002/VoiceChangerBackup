package com.mtg.app.voicechanger.extensions

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.widget.TextView



object ExtensionKt {
    fun  /* synthetic */`$r8$lambda$pw46Xnh7DxzzgbYJ1XRpgJlqE3w`(textView: TextView, d: Double) {
        `setGradientColor$lambda$0`(textView, d)
    }

//    fun setGradientColor(textView: TextView) {
//        Intrinsics.checkNotNullParameter(textView, "<this>")
//        val radians = Math.toRadians(100.0)
//        textView.post { `$r8$lambda$pw46Xnh7DxzzgbYJ1XRpgJlqE3w`(textView, radians) }
//    }

    fun `setGradientColor$lambda$0`(this_setGradientColor: TextView, d: Double) {
        val measuredWidth = this_setGradientColor.measuredWidth.toDouble()
        this_setGradientColor.paint.shader = LinearGradient(
            0.0f,
            0.0f,
            (Math.sin(d) * measuredWidth).toFloat(),
            (Math.cos(d) * measuredWidth).toFloat(),
            intArrayOf(Color.parseColor("#ffaa4d"), Color.parseColor("#ffdb76")),
            null as FloatArray?,
            Shader.TileMode.CLAMP
        )
        this_setGradientColor.invalidate()
    }

    fun nextInt(random: java.util.Random, i: Int, i2: Int): Int {
        return random.nextInt(i2 - i) + i
    }

    fun convertTwoDigits(j: Long): String {
        val sb: StringBuilder
        if (j < 10) {
            sb = StringBuilder()
            sb.append('0')
            sb.append(j)
        } else {
            sb = StringBuilder()
            sb.append(j)
            sb.append("")
        }
        return sb.toString()
    }

    fun startFlashScreenAnimation(view: View?) {
        view?.let {
            val alphaAnimation = AlphaAnimation(0.9f, 0.0f)
            alphaAnimation.duration = 500L
            alphaAnimation.interpolator = LinearInterpolator()
            alphaAnimation.repeatCount = AlphaAnimation.INFINITE
            alphaAnimation.repeatMode = AlphaAnimation.REVERSE

            it.startAnimation(alphaAnimation)
            it.visibility = View.VISIBLE
        }
    }
}