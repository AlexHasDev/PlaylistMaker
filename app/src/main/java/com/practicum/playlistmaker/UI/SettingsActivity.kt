package com.practicum.playlistmaker.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val share = findViewById<TextView>(R.id.share)
        val sendToSupport = findViewById<TextView>(R.id.sent_to_support)
        val termsOfUse = findViewById<TextView>(R.id.terms_of_use)
        val arrowBack = findViewById<ImageView>(R.id.settings_arrow_back)

        val url = "https://practicum.yandex.ru/profile/android-developer/"


        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra("Share", url)
            startActivity(shareIntent)

            val chooser = Intent.createChooser(shareIntent, getString(R.string.share_title))
            startActivity(chooser)
        }

        sendToSupport.setOnClickListener {
            val message = getString(R.string.message_support)
            val sandIntent = Intent(Intent.ACTION_SENDTO)
            sandIntent.data = Uri.parse("mailto:")
            sandIntent.putExtra(Intent.EXTRA_EMAIL, "alekseykhulla@yandex.ru")
            sandIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(sandIntent)
        }

        termsOfUse.setOnClickListener {
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW)
            termsOfUseIntent.data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(termsOfUseIntent)
        }

        arrowBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}