package com.practicum.playlistmaker.data.repository.sharedprefs

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.practicum.playlistmaker.data.repository.appSettings.SEARCH_STORY_KEY
import com.practicum.playlistmaker.data.repository.appSettings.storyPreference
import com.practicum.playlistmaker.domain.models.Track

object CreateSharedPreferences {
    @RequiresApi(Build.VERSION_CODES.N)
    fun saveSearchStoryPreference(track: Track) {
        val storyListPreference: Array<Track>? =
            if (createTrackListFromJson() == null) arrayOf() else createTrackListFromJson()
        val storyList: ArrayList<Track> = arrayListOf()
        if (storyListPreference != null) {
            storyList.addAll(storyListPreference)
        }

        storyList.removeIf { element -> element == track }
        storyList.add(track)

        if (storyList.size > 10) {
            storyList.removeAt(0)
        }

        storyPreference.edit()
            .putString(SEARCH_STORY_KEY, createJsonFromTrackList(storyList))
            .apply()
    }

    private fun createTrackListFromJson(): Array<Track>? {
        val sharedTrackList = storyPreference.getString(SEARCH_STORY_KEY, null)
        return Gson().fromJson(sharedTrackList, Array<Track>::class.java)
    }

    fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

}