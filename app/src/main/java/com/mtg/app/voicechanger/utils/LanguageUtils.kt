package com.mtg.app.voicechanger.utils

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.data.model.ItemLanguage
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.utils.app.AppPreferences
import com.mtg.app.voicechanger.utils.constant.Constants
import java.util.Locale

object LanguageUtils {
    private var appPreferences = AppPreferences.instance
    val listCountry: List<ItemLanguage>
        get() {
            val mList: MutableList<ItemLanguage> = ArrayList()
            mList.add(
                ItemLanguage(
                    R.drawable.flag_zh,
                    "Chinese",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "zh"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_en,
                    "English (UK)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "en"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_us,
                    "English (US)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "en"
                )
            )
//            mList.add(ItemLanguage(R.drawable.flag_ar, "Arabic", R.drawable.ic_disable, "ar"))
            mList.add(
                ItemLanguage(
                    R.drawable.flag_canada,
                    "French (Canada)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "fr"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_fr,
                    "French (France)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "fr"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_de,
                    "German",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "de"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_ja,
                    "Japanese",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "ja"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_pt,
                    "Portugal",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "pt"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_brazil,
                    "Portugal (Brazil)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "pt"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_spanish_normal,
                    "Spanish (Spain)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "es"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_es,
                    "Spanish (Latinh)",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "es"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_vi,
                    "Vietnamese",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "vi"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_bg,
                    "Bulgarian",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "bg"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_cs,
                    "Czech",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "cs"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_el,
                    "Greek",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "el"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_hi,
                    "Hindi",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "hi"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_in,
                    "Indonesian",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "in"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_it,
                    "Italian",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "it"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_ko,
                    "Korean",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "ko"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_nl,
                    "Dutch",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "nl"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_pl,
                    "Polish",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "pl"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_ru,
                    "Russian",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "ru"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_ro,
                    "Romanian",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "ro"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_sv,
                    "Swedish",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "sv"
                )
            )
            mList.add(
                ItemLanguage(
                    R.drawable.flag_th,
                    "Thai",
                    R.drawable.ic_disable,
                    R.drawable.bg_item_language,
                    "th"
                )
            )

            return mList
        }

    fun getFlagResourceID(context: Context): Int {
        val itemLanguage = listCountry.findLast { it.languageToLoad.equals(appPreferences.currentLanguage, true) } ?: return R.drawable.flag_en
        return itemLanguage.imageFlag
    }

    fun getFlagIcon(context: Context): Int {
        val mList: List<ItemLanguage> = listCountry
        val currentCode =
            context.getSharedPreferences(Constants.SHARE_PREF_LANGUAGE, Context.MODE_PRIVATE)
                .getString(Constants.SHARE_PREF_LANGUAGE, "English (US)")
        for (i in mList.indices) {
//            if (mList[i].getLanguageToLoad().equals(currentCode)) {
//                return mList[i].getImageFlag()
//            }
            if (mList[i].languageToLoad.equals(currentCode)) {
                return mList[i].imageFlag
            }
        }
        return 0
    }

    fun getCurrentLanguageCode(context: Context): String {
        val languageName = SharedPrefs.getString(context, Constants.SHARE_PREF_LANGUAGE, "default")
        val itemLanguage =
            listCountry.findLast { it.name.equals(languageName, true) } ?: return "en"
        return itemLanguage.languageToLoad
    }

    open fun getDefaultLanguage(): String{
        val userLang = Resources.getSystem().configuration.locales.get(0).language
        if (!TextUtils.isEmpty(userLang) && checkLanguageAvailable(userLang)) {
            return userLang
        } else {
            return Locale.ENGLISH.language
        }
    }

    private fun checkLanguageAvailable(userLang: String): Boolean {
        for (language in listCountry) {
            if (language.languageToLoad == userLang) {
                return true
            }
        }
        return false
    }

    fun getDefaultItemLanguage(): ItemLanguage? {
        return listCountry.find { itemLanguage -> itemLanguage.languageToLoad == getDefaultLanguage() }
    }
}