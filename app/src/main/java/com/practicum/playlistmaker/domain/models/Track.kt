package com.practicum.playlistmaker.domain.models

data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?, //duration
    val artworkUrl100: String?,  //albumImage
    val country: String?,
    val primaryGenreName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val previewUrl: String?
)

