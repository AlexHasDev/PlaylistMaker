package com.practicum.playlistmaker.UI

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.Data.StoryRecyclerAdapter.StoryRecyclerAdapter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Data.RecyclerSearch.SearchRecyclerAdapter
import com.practicum.playlistmaker.Data.retrofitSearch.ITunesApi
import com.practicum.playlistmaker.Data.Track
import com.practicum.playlistmaker.Data.retrofitSearch.SearchResponse
import com.practicum.playlistmaker.appSettings.App
import com.practicum.playlistmaker.appSettings.SEARCH_STORY_KEY
import com.practicum.playlistmaker.appSettings.SEARCH_STORY_PREFERENCE
import com.practicum.playlistmaker.appSettings.storyPreference
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var countValue: String = ""
    private lateinit var searchText: String
    private lateinit var arrowBack: ImageView
    private lateinit var interceptor: HttpLoggingInterceptor

    //search
    private lateinit var searchRecycler: RecyclerView
    private lateinit var searchResults: ArrayList<Track>
    private lateinit var clearButton: ImageView
    private lateinit var inputEditSearchText: EditText
    private lateinit var iTunesService: ITunesApi

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

        //preference
        searchStoryPreference = getSharedPreferences(SEARCH_STORY_PREFERENCE, MODE_PRIVATE)

        //OkHttp
        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        //system
        clearButton = findViewById(R.id.clearIcon)
        arrowBack = findViewById(R.id.search_arrow_back)


        inputEditSearchText = findViewById(R.id.search_edit_text)
        searchResults = arrayListOf()


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
        iTunesService = searchRetrofit.create(ITunesApi::class.java)


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



        if (savedInstanceState != null)
            countValue = savedInstanceState.getString(SEARCH_STATE, countValue)


        clearButton.setOnClickListener {
            inputEditSearchText.setText("")
            searchResults.clear()
            searchRecycler.adapter = SearchRecyclerAdapter(searchResults)
            placeholder.visibility = View.GONE
        }





        countValue = inputEditSearchText.toString()


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchStoryView.visibility = View.GONE
                clearButton.visibility = clearButtonVisibility(s)
                inputEditSearchText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {

                        search()
                        true
                    }
                    false
                }


            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputEditSearchText.addTextChangedListener(simpleTextWatcher)

        arrowBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


    private fun search() {

        if (inputEditSearchText.text.isNotEmpty())
            searchText = inputEditSearchText.text.toString()

        iTunesService.getSong(searchText.trim())
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (searchText.isNotEmpty()) {
                        when (response.code()) {
                            200 -> if (response.body()?.results!!.isNotEmpty()) {
                                searchResults.clear()
                                searchResults.addAll(response.body()?.results!!)
                                searchRecycler.adapter = SearchRecyclerAdapter(searchResults)
                                placeholder.visibility = View.GONE
                            } else {
                                setPlaceholder(searchResults = searchResults, onConnect = true)
                            }

                            else -> {
                                setPlaceholder(searchResults = searchResults, onConnect = false)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    setPlaceholder(searchResults = searchResults, onConnect = false)
                    Log.d(TAG, t.toString())
                }

            })
    }

    private fun clearSearchStory() {
        val clearedStoryList: Array<Track> = arrayOf()
        clearStoryButton.setOnClickListener {
            val clearedStory: ArrayList<Track> = arrayListOf()
            searchStoryPreference.edit().putString(
                SEARCH_STORY_KEY, App().createJsonFromTrackList(clearedStory)
            )
                .apply()
            storyRecycler.adapter = StoryRecyclerAdapter(clearedStoryList)
            searchStoryView.visibility = View.GONE
        }
    }

    private fun showStoryList() {
        if (storyList.isNotEmpty()) {
            inputEditSearchText.setOnFocusChangeListener { view, hasFocus ->
                searchStoryView.visibility = View.VISIBLE
                storyRecycler.adapter = StoryRecyclerAdapter(storyList)

            }

        }
    }

    private fun setPlaceholder(searchResults: ArrayList<Track>, onConnect: Boolean) {
        searchResults.clear()
        if (onConnect) {
            searchRecycler.adapter = SearchRecyclerAdapter(this.searchResults)
            placeholder.visibility = View.VISIBLE
            placeholderText.text = getString(R.string.nothing_found)
            placeholderImage.setImageDrawable(getDrawable(R.drawable.nothing_light))
            searchRefreshButton.visibility = View.GONE
        } else {
            searchRecycler.adapter = SearchRecyclerAdapter(this.searchResults)
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
        outState.putString(SEARCH_STATE, countValue)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val SEARCH_STATE = "SEARCH_STATE"
    }


}