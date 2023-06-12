package com.practicum.playlistmaker.UI

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Data.Track
import com.practicum.playlistmaker.Data.trackFromJson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.DateUtils
import com.practicum.playlistmaker.appSettings.TRACK_TO_PLAYER_KEY


class PlayerTrackActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REQUEST_TIME_TRACK_DELAY = 500L
    }

    //buttons
    lateinit var arrowBack: ImageView
    lateinit var playAndPauseButton: ImageButton

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
    var onPaused: Boolean = false

    //player
    lateinit var mainHandler: Handler
    var time: Long = 0L
    var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    var timeThread = Thread()


    @SuppressLint("MissingInflatedId")
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


        //buttons in activity
        arrowBack = findViewById(R.id.player_arrow_back)
        playAndPauseButton = findViewById(R.id.player_button_play_and_pause)
        playAndPauseButton.isEnabled = false
        arrowBack.setOnClickListener { finish() }
        playAndPauseButton.setOnClickListener {
            playbackControl()
            timeThread = Thread {
                mainHandler.post(trackStopwatch())
            }
            timeThread.start()
        }


        //track
        val receivedIntent = intent.getStringExtra(TRACK_TO_PLAYER_KEY)
        val trackForPlayer = trackFromJson(receivedIntent)
        mainHandler = Handler(Looper.getMainLooper())

        inflateTrack(trackForPlayer)

    }


    private fun inflateTrack(track: Track?) {
        trackName.text = track?.trackName
        artistName.text = track?.artistName
        album.text = track?.collectionName
        year.text = track?.releaseDate?.substring(0, 4)
        genre.text = track?.primaryGenreName
        country.text = track?.country
        duration.text = DateUtils.changeDateFormat(track?.trackTimeMillis)
        preparePlayer(track)
        trackTime.text = DateUtils.changeDateFormat(time.toString())
        Glide.with(this)
            .load(
                track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
            )
            .placeholder(R.drawable.snake)
            .transform(RoundedCorners(2))
            .into(imageTrack)
    }

    private fun startPlayer() {
        onPaused = false
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playAndPauseButton.setImageResource(R.drawable.baseline_pause_circle_24)

    }

    private fun pausePlayer() {
        onPaused = true
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playAndPauseButton.setImageResource(R.drawable.baseline_play_circle_24)

    }

    private fun preparePlayer(track: Track?) {
        playAndPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playAndPauseButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playAndPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
            playerState = STATE_PREPARED
            onPaused = true
            setDefaultTimeTrack()
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainHandler.removeCallbacks(timeThread)
    }

    private fun trackStopwatch(): Runnable {

        return object : Runnable {
            override fun run() {
                if(!onPaused) {
                    time = mediaPlayer.currentPosition.toLong()
                    trackTime.text = DateUtils.changeDateFormat(time.toString())
                    mainHandler.postDelayed(this, REQUEST_TIME_TRACK_DELAY)
                }
            }
        }
    }

    private fun setDefaultTimeTrack(){
        trackTime.text = DateUtils.changeDateFormat("0")
    }
}




