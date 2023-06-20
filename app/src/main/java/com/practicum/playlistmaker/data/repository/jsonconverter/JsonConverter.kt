package com.practicum.playlistmaker.data.repository.jsonconverter

import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track

object JsonConverter {
    fun trackToJson(track: Track): String? {
        return Gson().toJson(track)
    }

    fun trackFromJson(jsonTrack: String?): Track {
        return Gson().fromJson(jsonTrack, Track::class.java)
    }
}