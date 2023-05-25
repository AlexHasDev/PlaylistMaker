package com.practicum.playlistmaker.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?, //duration
    val artworkUrl100: String?,  //albumImage
    val country: String?,
    val primaryGenreName: String?,
    val collectionName: String?,
    val releaseDate: String?
)

fun trackToJson(track: Track): String?{
    return Gson().toJson(track)
}

fun trackFromJson(jsonTrack: String?): Track{
    return Gson().fromJson(jsonTrack, Track::class.java)
}