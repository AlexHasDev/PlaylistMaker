package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.player.playerManger.PlayerManager

class PlayerManageUseCase(private val playerManager: PlayerManager, private val track: Track?) {

    fun turnOnController(){
        playerManager.playbackControl()
    }
    fun prepare(){
        playerManager.preparePlayerForStart(track = this.track)
    }

    fun pausePlayer() {
        playerManager.pausePlayer()
    }
}