package com.practicum.playlistmaker.Data

data class Track (
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String?,
    val artworkUrl100: String
)
    fun makeTrack(id: String, name: String, artist: String, time: String?, image: String): Track {
        return Track(id, name, artist, time, image)

}