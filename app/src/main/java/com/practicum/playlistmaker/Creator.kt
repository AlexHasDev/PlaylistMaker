package com.practicum.playlistmaker

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.data.network.retrofitClient.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.playerImpl.playerManagerImpl.PlayerManagerImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.models.PlayerState
import com.practicum.playlistmaker.domain.usecase.PlayerManageUseCase
import com.practicum.playlistmaker.domain.usecase.TracksInteractor

object Creator {

    val mediaPlayer = MediaPlayer()
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun providerTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun playerManageUseCase(playerState: PlayerState, track: Track): PlayerManageUseCase {
        return PlayerManageUseCase(PlayerManagerImpl(mediaPlayer, playerState), track)
    }
}