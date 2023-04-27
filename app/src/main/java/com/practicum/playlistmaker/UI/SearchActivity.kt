package com.practicum.playlistmaker.UI

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.RecyclerSearch.SearchRecyclerAdapter
import com.practicum.playlistmaker.retrofitSearch.ITunesApi
import com.practicum.playlistmaker.retrofitSearch.Track
import com.practicum.playlistmaker.retrofitSearch.TracksResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var countValue: String = ""


    lateinit var searchRecycler: RecyclerView
    lateinit var results: ArrayList<Track>
    lateinit var placeholderText: TextView
    lateinit var placeholder: LinearLayout
    lateinit var placeholderImage: ImageView
    lateinit var searchRefreshButton: Button
    lateinit var inputEditSearchText: EditText
    private lateinit var iTunesService: ITunesApi

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val arrowBack = findViewById<ImageView>(R.id.search_arrow_back)

        inputEditSearchText = findViewById(R.id.search_edit_text)
        placeholder = findViewById(R.id.placeholder_trackList)
        placeholderImage = findViewById(R.id.placeholder_trackList_image)
        placeholderText = findViewById(R.id.search_placeholder_text)
        searchRefreshButton = findViewById(R.id.search_refresh_button)

        //retrofit
        val searchRetrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())

            .build()

        //itunes запросы
        iTunesService = searchRetrofit.create(ITunesApi::class.java)


        results = arrayListOf()

        searchRecycler = findViewById(R.id.search_recycler)




        if (savedInstanceState != null)
            countValue = savedInstanceState.getString(SEARCH_STATE, countValue)


        clearButton.setOnClickListener {
            inputEditSearchText.setText("")
            results.clear()
            searchRecycler.adapter = SearchRecyclerAdapter(results)
            placeholder.visibility = View.GONE
        }

        countValue = inputEditSearchText.toString()


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

    fun search() {
        iTunesService.getSong(inputEditSearchText.text.toString().trim())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (inputEditSearchText.text.isNotEmpty()) {
                        when (response.code()) {
                            200 -> if (response.body()?.results!!.isNotEmpty()) {
                                results.clear()
                                results.addAll(response.body()?.results!!)
                                searchRecycler.adapter = SearchRecyclerAdapter(results)
                                placeholder.visibility = View.GONE
                            } else {
                                setPlaceholder(searchResults = results, onConnect = true)
                            }

                            else -> {
                                setPlaceholder(searchResults = results, onConnect = false)
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    setPlaceholder(searchResults = results, onConnect = false)
                    Log.d(TAG, t.toString())
                }

            })
    }

    fun setPlaceholder(searchResults: ArrayList<Track>, onConnect: Boolean) {
        if (onConnect) {
            searchResults.clear()
            searchRecycler.adapter = SearchRecyclerAdapter(results)
            placeholder.visibility = View.VISIBLE
            placeholderText.text = getString(R.string.nothing_found)
            placeholderImage.setImageDrawable(getDrawable(R.drawable.nothing_light))
            searchRefreshButton.visibility = View.GONE
        } else {
            searchResults.clear()
            searchRecycler.adapter = SearchRecyclerAdapter(results)
            placeholder.visibility = View.VISIBLE
            placeholderText.text = getString(R.string.connect_problem)
            placeholderImage.setImageDrawable(getDrawable(R.drawable.practicum_problem_light))
            searchRefreshButton.visibility = View.VISIBLE
            searchRefreshButton.setOnClickListener{search()}
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STATE, countValue)
    }


    fun clearButtonVisibility(s: CharSequence?): Int {
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