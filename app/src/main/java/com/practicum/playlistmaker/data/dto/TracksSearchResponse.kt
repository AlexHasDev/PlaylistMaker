package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track

data class TracksSearchResponse(
    val results: ArrayList<Track>,
//    val searchType: String,
//    val expression: String
) : Response()



