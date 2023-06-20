package com.practicum.playlistmaker.domain.repository.player.playerManger

import com.practicum.playlistmaker.domain.models.Track

interface PlayerManager {

    fun startPlayer()
    fun pausePlayer()
    fun preparePlayerForStart(track: Track?)
    fun playbackControl()
}