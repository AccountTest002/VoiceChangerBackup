package com.mtg.app.voicechanger.interfaces

import com.mtg.app.voicechanger.data.model.Effect

interface EffectListener {
    fun addEffectListener(effect: Effect?)
}
