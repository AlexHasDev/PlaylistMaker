package com.practicum.playlistmaker.domain.repository.player.playerManger

class PlayerState private constructor(var state: Int) {

    constructor() : this(STATE_DEFAULT)

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    fun changeStateToPrepared() {
        state = STATE_PREPARED
    }
    fun changeStateToPlaying() {
        state = STATE_PLAYING
    }
    fun changeStateToPaused(){
        state = STATE_PAUSED
    }
}