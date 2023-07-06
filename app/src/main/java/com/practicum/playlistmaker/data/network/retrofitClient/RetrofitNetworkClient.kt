package com.practicum.playlistmaker.data.network.retrofitClient

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.network.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            val response = iTunesService.getSong(dto.expression).execute()

            val body = response.body() ?: Response()

            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}