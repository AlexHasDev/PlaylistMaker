package com.practicum.playlistmaker.data.repository.utils

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R

object GlideUtils {

    fun setLargeImage(address: String?, into: ImageView, activity: Activity){
        Glide.with(activity)
            .load(
                address?.replaceAfterLast('/', "512x512bb.jpg")
            )
            .placeholder(R.drawable.snake)
            .transform(RoundedCorners(2))
            .into(into)
    }
}