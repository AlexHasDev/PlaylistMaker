package com.practicum.playlistmaker.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.appSettings.App
import com.practicum.playlistmaker.appSettings.SETTINGS_KEY_FOR_EDIT
import com.practicum.playlistmaker.appSettings.SETTINGS_PREFERENCE

class SettingsActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val share = findViewById<TextView>(R.id.share)
        val sendToSupport = findViewById<TextView>(R.id.sent_to_support)
        val termsOfUse = findViewById<TextView>(R.id.terms_of_use)
        val arrowBack = findViewById<ImageView>(R.id.settings_arrow_back)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        val url = "https://practicum.yandex.ru/profile/android-developer/"

        val sharedPreference = getSharedPreferences(SETTINGS_PREFERENCE, MODE_PRIVATE)
        themeSwitcher.isChecked = sharedPreference.getBoolean(SETTINGS_KEY_FOR_EDIT, App().darkTheme)
        themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }


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
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, "alekseykhulla@yandex.ru")
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(sendIntent)
        }

        termsOfUse.setOnClickListener {
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW)
            termsOfUseIntent.data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(termsOfUseIntent)
        }

        arrowBack.setOnClickListener {
            finish()
        }

    }
}