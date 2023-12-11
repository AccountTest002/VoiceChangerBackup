package com.mtg.app.voicechanger.view.activity

import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.databinding.ActivitySavedBinding

class SavedActivity :
    BaseActivity<ActivitySavedBinding>(ActivitySavedBinding::inflate) {


    override fun initView() {
        fullScreen()
    }

    override fun addEvent() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun fullScreen() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val statusBackground = ContextCompat.getDrawable(this, R.drawable.bg_head_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        window.setBackgroundDrawable(statusBackground)
    }

}