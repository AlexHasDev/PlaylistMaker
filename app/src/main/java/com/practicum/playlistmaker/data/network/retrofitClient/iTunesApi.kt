package com.practicum.playlistmaker.data.network.retrofitClient

import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {

    @GET("search?entity=song")
    fun getSong(@Query("term") query: String): Call<TracksSearchResponse>
}