package com.practicum.playlistmaker.domain.impl

import com.bumptech.glide.util.Executors
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.usecase.TracksInteractor

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {


    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}