package com.practicum.playlistmaker.data.dto

import com.google.gson.Gson

data class TrackDto(
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

