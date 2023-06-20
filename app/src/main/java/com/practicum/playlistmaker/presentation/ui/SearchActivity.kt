package com.practicum.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.presentation.recyclers.storyRecycler.StoryRecyclerAdapter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.recyclers.searchRecycler.SearchRecyclerAdapter
import com.practicum.playlistmaker.data.network.retrofitClient.iTunesApi
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.repository.sharedprefs.CreateSharedPreferences
import com.practicum.playlistmaker.data.repository.appSettings.SEARCH_STORY_KEY
import com.practicum.playlistmaker.data.repository.appSettings.SEARCH_STORY_PREFERENCE
import com.practicum.playlistmaker.data.repository.appSettings.TRACK_TO_PLAYER_KEY
import com.practicum.playlistmaker.data.repository.appSettings.storyPreference
import com.practicum.playlistmaker.data.repository.jsonconverter.JsonConverter
import com.practicum.playlistmaker.domain.models.Track
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(), SearchRecyclerAdapter.TrackListener {

    //system
    private var defaultSearchText: String = ""
    private lateinit var searchText: String
    private lateinit var interceptor: HttpLoggingInterceptor
    private lateinit var mainHandler: Handler

    //ui elements
    private lateinit var arrowBack: ImageView
    //search
    private lateinit var searchRecycler: RecyclerView
    private lateinit var searchResults: ArrayList<Track>
    private lateinit var clearButton: ImageView
    private lateinit var inputEditSearchText: EditText
    private lateinit var iTunesService: iTunesApi
    private lateinit var searchRunnable: Runnable
    private lateinit var searchingProgressBar: ProgressBar

    private val searchIteractor = Creator.providerTracksInteractor()

    private var isClickAllowed = true
    //searching placeholder
    private lateinit var placeholderText: TextView
    private lateinit var placeholder: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var searchRefreshButton: Button

    //search story
    private lateinit var clearStoryButton: Button
    private lateinit var searchStoryView: LinearLayout
    private lateinit var storyList: Array<Track>
    private lateinit var storyRecycler: RecyclerView
    private lateinit var searchStoryPreference: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //handler
        mainHandler = Handler(Looper.getMainLooper())

        //preference
        searchStoryPreference = getSharedPreferences(SEARCH_STORY_PREFERENCE, MODE_PRIVATE)

        //OkHttp
        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        //Ux elements
        clearButton = findViewById(R.id.clearIcon)
        arrowBack = findViewById(R.id.search_arrow_back)

        //search
        inputEditSearchText = findViewById(R.id.search_edit_text)
        searchResults = arrayListOf()
        searchingProgressBar = findViewById(R.id.searching_progress_bar)
        searchRunnable = Runnable { search() }

        //placeholder
        placeholder = findViewById(R.id.placeholder_trackList)
        placeholderImage = findViewById(R.id.placeholder_trackList_image)
        placeholderText = findViewById(R.id.search_placeholder_text)
        searchRefreshButton = findViewById(R.id.search_refresh_button)

        //retrofit
        val searchRetrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        iTunesService = searchRetrofit.create(iTunesApi::class.java)

        //recyclers
        searchRecycler = findViewById(R.id.search_recycler)
        storyRecycler = findViewById(R.id.search_story_recycler)
        storyRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        //searchStory
        val sharedStoryTrackList = storyPreference.getString(SEARCH_STORY_KEY, null)
        searchStoryView = findViewById(R.id.search_story_view)
        storyList =
            if (sharedStoryTrackList == null) arrayOf()
            else Gson().fromJson(sharedStoryTrackList, Array<Track>::class.java)
        clearStoryButton = findViewById(R.id.clear_story_button)
        showStoryList()
        clearSearchStory()


        if (savedInstanceState != null) {
            defaultSearchText = savedInstanceState.getString(SEARCH_STATE, defaultSearchText)
        }
            defaultSearchText = inputEditSearchText.toString()


        clearButton.setOnClickListener {
            inputEditSearchText.setText("")
            clearSearchResults()
            placeholder.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchStoryView.visibility = View.GONE
                clearButton.visibility = clearButtonVisibility(s)

                searchDebounce()

                if(inputEditSearchText.text.toString() == "")
                    clearSearchResults()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        inputEditSearchText.addTextChangedListener(simpleTextWatcher)

        arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(searchRunnable)
        super.onDestroy()
    }

    private fun search() {
        if (searchStoryView.visibility == View.GONE){
            if (inputEditSearchText.text.isNotEmpty()) {
                searchText = inputEditSearchText.text.toString()
                clearSearchResults()
                placeholder.visibility = View.GONE
                searchingProgressBar.visibility = View.VISIBLE
            }
        iTunesService.getSong(searchText.trim())
            .enqueue(object : Callback<TracksSearchResponse> {
                override fun onResponse(
                    call: Call<TracksSearchResponse>,
                    response: Response<TracksSearchResponse>
                ) {
                    if (searchText.isNotEmpty()) {
                        searchingProgressBar.visibility = View.GONE
                        when (response.code()) {
                            200 -> if (response.body()?.results!!.isNotEmpty()) {
                                searchResults.clear()
                                searchResults.addAll(response.body()?.results!!)
                                searchRecycler.adapter =
                                    SearchRecyclerAdapter(searchResults, this@SearchActivity)
                                placeholder.visibility = View.GONE
                            } else {
                                setPlaceholder(searchResults = searchResults, onConnect = true)
                            }

                            else -> {
                                setPlaceholder(searchResults = searchResults, onConnect = true)

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
                    searchingProgressBar.visibility = View.GONE
                    setPlaceholder(searchResults = searchResults, onConnect = false)
                    Log.d(TAG, t.toString())
                }

            })
    }
    }

    private fun clearSearchStory() {
        val clearedStoryList: Array<Track> = arrayOf()
        clearStoryButton.setOnClickListener {
            val clearedStory: ArrayList<Track> = arrayListOf()
            searchStoryPreference.edit().putString(
                SEARCH_STORY_KEY, CreateSharedPreferences.createJsonFromTrackList(clearedStory)
            )
                .apply()
            storyRecycler.adapter = StoryRecyclerAdapter(clearedStoryList, this)
            searchStoryView.visibility = View.GONE
        }
    }

    private fun showStoryList() {
        if (storyList.isNotEmpty()) {
            inputEditSearchText.setOnFocusChangeListener { view, hasFocus ->
                searchStoryView.visibility = View.VISIBLE
                storyRecycler.adapter = StoryRecyclerAdapter(storyList, this)
            }
        }
    }

    private fun setPlaceholder(searchResults: ArrayList<Track>, onConnect: Boolean) {
        searchResults.clear()
            if (onConnect) {
                searchRecycler.adapter = SearchRecyclerAdapter(this.searchResults, this)
                placeholder.visibility = View.VISIBLE
                placeholderText.text = getString(R.string.nothing_found)
                placeholderImage.setImageDrawable(getDrawable(R.drawable.nothing_light))
                searchRefreshButton.visibility = View.GONE

            } else {
                searchRecycler.adapter = SearchRecyclerAdapter(this.searchResults, this)
                placeholder.visibility = View.VISIBLE
                placeholderText.text = getString(R.string.connect_problem)
                placeholderImage.setImageDrawable(getDrawable(R.drawable.practicum_problem_light))
                searchRefreshButton.visibility = View.VISIBLE
                searchRefreshButton.setOnClickListener {
                    search()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STATE, defaultSearchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(track: Track) {
        if(isClickAllowed) {
            clickDebounce()
            CreateSharedPreferences.saveSearchStoryPreference(track)
            val trackIntent = Intent(this, PlayerTrackActivity::class.java)
            trackIntent.putExtra(TRACK_TO_PLAYER_KEY, JsonConverter.trackToJson(track))
            startActivity(trackIntent)
        }
    }

    private fun searchDebounce() {
        mainHandler.removeCallbacks(searchRunnable)
        if(inputEditSearchText.text.toString() != "") {
            mainHandler.postDelayed(searchRunnable, SEARCH_DELAY)
        }
    }

    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            mainHandler.postDelayed({isClickAllowed = true}, CLICK_DELAY)
        }
        return current
    }


    private fun clearSearchResults(){
        searchResults.clear()
        searchRecycler.adapter = SearchRecyclerAdapter(searchResults, this)
    }

    companion object {
        const val SEARCH_STATE = "SEARCH_STATE"
        const val CLICK_DELAY = 1000L
        const val SEARCH_DELAY = 2000L
    }
}