package com.mtg.app.voicechanger.utils

import android.content.Context
import android.content.SharedPreferences
import com.mtg.app.voicechanger.utils.constant.Constants

class Settings private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.APP_NAME, 0)

    var bgGun: String?
        get() = sharedPreferences.getString(Constants.BACKGROUND_GUN, "").toString()
        set(value) {
            val editor = sharedPreferences.edit()
            editor.putString(Constants.BACKGROUND_GUN, value)
            editor.apply()
        }

    companion object {
        private var instance: Settings? = null

        fun getInstance(context: Context): Settings {
            if (instance == null) {
                instance = Settings(context)
            }
            return instance!!
        }
    }
}
