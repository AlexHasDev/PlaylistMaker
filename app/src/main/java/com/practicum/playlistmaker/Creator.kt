package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.data.network.retrofitClient.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.usecase.TracksInteractor

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun providerTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}