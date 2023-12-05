package com.mtg.app.voicechanger.utils.app

import android.content.Context
import com.google.gson.Gson
import com.mtg.app.voicechanger.utils.LanguageUtils
import com.mtg.app.voicechanger.utils.constant.Constants.KEY_CHOOSE_LANGUAGE
import com.mtg.app.voicechanger.utils.constant.Constants.KEY_CURRENT_LANGUAGE


class AppPreferences(val context: Context, val mGson: Gson = Gson()) :
    BasePreferences(context, context.packageName) {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: AppPreferences
    }

    inline var currentLanguage: String
        get() {
            return getString(KEY_CURRENT_LANGUAGE, LanguageUtils.getDefaultLanguage())
        }
        set(value) {
            putString(KEY_CURRENT_LANGUAGE, value)
        }
    inline var isChooseLanguage: Boolean
        get() {
            return getBoolean(KEY_CHOOSE_LANGUAGE, false)
        }
        set(value) {
            putBoolean(KEY_CHOOSE_LANGUAGE, true)
        }
}