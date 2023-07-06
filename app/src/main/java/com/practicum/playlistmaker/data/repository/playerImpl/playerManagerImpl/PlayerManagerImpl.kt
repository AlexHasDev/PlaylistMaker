package com.practicum.playlistmaker.data.repository.playerImpl.playerManagerImpl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.player.playerManger.PlayerManager
import com.practicum.playlistmaker.domain.models.PlayerState


class PlayerManagerImpl(
    var mediaPlayer: MediaPlayer,
    var playerState: PlayerState
) : PlayerManager {

    override fun startPlayer() {
        mediaPlayer.start()
        playerState.changeStateToPlaying()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState.changeStateToPaused()
    }

    override fun preparePlayerForStart(track: Track?) {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.changeStateToPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState.changeStateToPrepared()
        }
    }

    override fun playbackControl() {
        when (playerState.state) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    override fun getCurrentPosition() : Long {
        return mediaPlayer.currentPosition.toLong()
    }
}

