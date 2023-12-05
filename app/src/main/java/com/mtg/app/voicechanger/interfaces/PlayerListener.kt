package com.mtg.app.voicechanger.interfaces

interface PlayerListener {
    fun setNewPath(path: String?)
    fun start()
    fun pause()
    fun resume()
    fun stop()
    fun release()
}
