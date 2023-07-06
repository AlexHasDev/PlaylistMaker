package com.practicum.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.models.PlayerState
import com.practicum.playlistmaker.domain.usecase.PlayerManageUseCase
import com.practicum.playlistmaker.presentation.presentationKeys.SearchStoryKeys.TRACK_TO_PLAYER_KEY
import com.practicum.playlistmaker.presentation.ui.utils.DateUtils
import com.practicum.playlistmaker.presentation.ui.utils.GlideUtils

class PlayerTrackActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_TIME_TRACK_DELAY = 500L
    }

    //trackUi
    private lateinit var imageTrack: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    lateinit var trackTime: TextView
    private lateinit var playAndPauseButton: ImageButton

    //player
    lateinit var mainHandler: Handler
    //var mediaPlayer = MediaPlayer()
    private var timeForProgress: Long = 0L
    private var timeThread = Thread()
    var playerState = PlayerState()
    lateinit var playerManageUseCase: PlayerManageUseCase

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
        val arrowBack: ImageView = findViewById(R.id.player_arrow_back)
        arrowBack.setOnClickListener { finish() }
        playAndPauseButton = findViewById(R.id.player_button_play_and_pause)
        playAndPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
        playAndPauseButton.setOnClickListener {
            playerManageUseCase.turnOnController()
            timeThread = Thread {
                mainHandler.post(uiTrackProgressThread())
            }
            timeThread.start()
        }

        mainHandler = Handler(Looper.getMainLooper())

        //loadTrack
        val receivedIntent = intent.getParcelableExtra(TRACK_TO_PLAYER_KEY) ?: Track()
        val trackForPlayer = receivedIntent

        //useCase
        playerManageUseCase = Creator.playerManageUseCase(playerState, trackForPlayer)

        fillTrackContent(track = trackForPlayer)
    }

    private fun fillTrackContent(track: Track?) {

        trackName.text = track?.trackName
        artistName.text = track?.artistName
        album.text = track?.collectionName
        year.text = track?.releaseDate?.substring(0, 4)
        genre.text = track?.primaryGenreName
        country.text = track?.country
        duration.text = DateUtils.changeDateFormat(track?.trackTimeMillis)
        trackTime.text = DateUtils.changeDateFormat(timeForProgress.toString())
        GlideUtils.setLargeImage(
            address = track?.artworkUrl100,
            into = imageTrack,
            activity = this
        )
        playerManageUseCase.prepare()
    }

    private fun changePlayAndPauseButtonFromStatus() {
        when (playerState.state) {
            PlayerState.STATE_PLAYING -> playAndPauseButton.setImageResource(R.drawable.baseline_pause_circle_24)
            PlayerState.STATE_PAUSED -> playAndPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
            PlayerState.STATE_PREPARED -> playAndPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
        }
    }

    override fun onPause() {
        super.onPause()
        playerManageUseCase.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(timeThread)
    }

    private fun uiTrackProgressThread(): Runnable {
        return object : Runnable {
            override fun run() {
                timeForProgress = if(playerState.state == PlayerState.STATE_PREPARED) 0L else playerManageUseCase.getCurrentPosition()
                trackTime.text = DateUtils.changeDateFormat(timeForProgress.toString())
                changePlayAndPauseButtonFromStatus()
                mainHandler.postDelayed(this, REQUEST_TIME_TRACK_DELAY)
            }
        }
    }
}




