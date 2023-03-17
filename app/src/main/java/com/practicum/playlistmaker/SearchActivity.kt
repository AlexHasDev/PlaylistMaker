package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    var countValue: String = ""
    companion object {
        const val SEARCH_STATE = "SEARCH_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val inputEditText = findViewById<EditText>(R.id.search_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        if (savedInstanceState != null)
            countValue = savedInstanceState.getString(SEARCH_STATE, countValue)


        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        countValue = inputEditText.toString()


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
}