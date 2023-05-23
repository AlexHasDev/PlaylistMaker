package com.practicum.playlistmaker.UI

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Data.Track
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.appSettings.CreateSharedPreferences
import com.practicum.playlistmaker.appSettings.TRACK_TO_PLAYER_KEY
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerTrackActivity : AppCompatActivity() {

    lateinit var arrowBack: ImageView

    //track
    lateinit var imageTrack: ImageView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var duration: TextView
    lateinit var album: TextView
    lateinit var year: TextView
    lateinit var genre: TextView
    lateinit var country: TextView
    lateinit var trackTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_track)

        //trackUi
        imageTrack = findViewById(R.id.player_album_image)
        trackName = findViewById(R.id.player_track_name)
        artistName = findViewById(R.id.player_artist_name)
        duration = findViewById(R.id.player_track_duration)
        album = findViewById(R.id.player_track_album)
        year = findViewById(R.id.player_track_year)
        genre = findViewById(R.id.player_track_genre)
        country = findViewById(R.id.player_track_country)
        trackTime = findViewById(R.id.player_track_time)

        //track
        val receivedIntent = intent
        val playerTrack = receivedIntent.getParcelableExtra<Track>(TRACK_TO_PLAYER_KEY)
        inflateTrack(playerTrack)

        //buttons
        arrowBack = findViewById(R.id.player_arrow_back)
        arrowBack.setOnClickListener { finish() }


    }
    fun inflateTrack(track: Track?){
        trackName.text = track?.trackName
        artistName.text = track?.artistName
        album.text = track?.collectionName
        year.text = track?.releaseDate?.substring(0, 4)
        genre.text = track?.primaryGenreName
        country.text = track?.country
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis?.toInt())
        trackTime.text = "0:00"
        Glide.with(this)
            .load(track?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
            )
            .placeholder(R.drawable.snake)
            .transform(RoundedCorners(2))
            .into(imageTrack)
    }
}