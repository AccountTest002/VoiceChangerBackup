package com.mtg.app.voicechanger.data.model

class Effect {
    var id = 0
    var src = 0
    var title: String? = null
    var changeVoice: String? = null

    constructor()
    constructor(id: Int, src: Int, title: String?, changeVoice: String?) {
        this.id = id
        this.src = src
        this.title = title
        this.changeVoice = changeVoice
    }
}
