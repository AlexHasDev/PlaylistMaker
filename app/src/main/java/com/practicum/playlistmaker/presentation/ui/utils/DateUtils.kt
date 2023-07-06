package com.practicum.playlistmaker.presentation.ui.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {

    fun changeDateFormat(date: String?) : String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(date?.toInt())
    }
}