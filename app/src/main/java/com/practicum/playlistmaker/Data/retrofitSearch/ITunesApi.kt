package com.practicum.playlistmaker.Data.retrofitSearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("search?entity=song")
    fun getSong(@Query("term") query: String): Call<SearchResponse>
}