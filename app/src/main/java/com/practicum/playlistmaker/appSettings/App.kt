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




}