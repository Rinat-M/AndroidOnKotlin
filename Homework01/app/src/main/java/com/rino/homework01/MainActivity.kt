package com.rino.homework01

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickMeButton = findViewById<Button>(R.id.click_me_button)
        val infoTextView = findViewById<TextView>(R.id.info_text_view)

        clickMeButton.setOnClickListener {
            infoTextView.text = getString(R.string.button_was_clicked)
        }
    }

}