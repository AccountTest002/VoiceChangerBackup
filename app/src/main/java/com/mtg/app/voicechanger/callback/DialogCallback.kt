package com.mtg.app.voicechanger.callback

interface DialogCallback {
    fun onAllow(value: Any?)

    fun onDeny(value: Any?)

    fun onDismiss(value: Any?)
}