package com.mtg.app.voicechanger.utils.constant

object Constants {
    val TAG: String = javaClass.name
    const val EMAIL = ""
    const val SUBJECT = ""
    const val POLICY_URL =
        ""
    const val PUBLISH_NAME = ""

    const val IS_RATE = "IS_RATE"
    const val COUNT_RATE = "COUNT_RATE"
    const val COUNT_OPEN_TOPIC = "COUNT_OPEN_TOPIC"
    const val COUNT_OPEN_SOUND_PLAY = "COUNT_OPEN_SOUND_PLAY"
    const val COUNT_OPEN_FAVORITE = "COUNT_OPEN_FAVORITE"

    const val KEY_CURRENT_LANGUAGE = "KEY_CURRENT_LANGUAGE_JDK"
    const val KEY_CHOOSE_LANGUAGE = "KEY_CHOOSE_LANGUAGE_JDK"

    fun getPolicyURL(): String? {
        return POLICY_URL
    }
}