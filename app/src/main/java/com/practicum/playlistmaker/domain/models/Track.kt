package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    var trackId: String? = null,
    var trackName: String? = null,
    var artistName: String? = null,
    var trackTimeMillis: String? = null, //duration
    var artworkUrl100: String? = null,  //albumImage
    var country: String? = null,
    var primaryGenreName: String? = null,
    var collectionName: String? = null,
    var releaseDate: String? = null,
    var previewUrl: String? = null
) : Parcelable

