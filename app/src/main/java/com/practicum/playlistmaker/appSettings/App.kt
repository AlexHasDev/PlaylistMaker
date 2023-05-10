package com.practicum.playlistmaker.appSettings

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.practicum.playlistmaker.Data.Track


const val SETTINGS_PREFERENCE = "SETTINGS_PREFERENCE"
const val SETTINGS_KEY_FOR_EDIT = "SETTINGS_KEY_FOR_EDIT"
const val SEARCH_STORY_PREFERENCE = "SEARCH_STORY_PREFERENCE"
const val SEARCH_STORY_KEY = "SEARCH_STORY_KEY"

lateinit var sharedPreference: SharedPreferences
lateinit var storyPreference: SharedPreferences

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        storyPreference = getSharedPreferences(SEARCH_STORY_PREFERENCE, MODE_PRIVATE)
        sharedPreference = getSharedPreferences(SETTINGS_PREFERENCE, MODE_PRIVATE)
        darkTheme = sharedPreference.getBoolean(SETTINGS_KEY_FOR_EDIT, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPreference.edit().putBoolean(SETTINGS_KEY_FOR_EDIT, darkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun saveSearchStoryPreference(track: Track) {
        val storyListPreference: Array<Track>? =
            if (createTrackListFromJson() == null) arrayOf() else createTrackListFromJson()
        val storyList: ArrayList<Track> = arrayListOf()
        if (storyListPreference != null) {
            for (i in storyListPreference) {
                storyList.add(i)
            }
        }
        storyList.removeIf { element -> element == track }
        storyList.add(track)

        if (storyList.size > 10) {
            storyList.removeAt(0)
        }

        storyPreference.edit().putString(
            SEARCH_STORY_KEY, createJsonFromTrackList(
                storyList
            )
        )
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