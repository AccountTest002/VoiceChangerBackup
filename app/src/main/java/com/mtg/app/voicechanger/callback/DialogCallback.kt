package com.mtgapp.image2pdf.callback

interface DialogCallback {
    fun onAllow(value: Any?)

    fun onDeny(value: Any?)

    fun onDismiss(value: Any?)
}