package com.practicum.playlistmaker.Data

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(country)
        parcel.writeString(primaryGenreName)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
fun makeTrack(
    id: String?,
    name: String?,
    artist: String?,
    time: String?,
    image: String?,
    country: String?,
    genre: String?,
    album: String?,
    year: String?
): Track {
    return Track(id, name, artist, time, image, country, genre, album, year)
}