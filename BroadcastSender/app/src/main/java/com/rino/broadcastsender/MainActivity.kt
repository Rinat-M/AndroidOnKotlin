package com.rino.broadcastsender

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rino.broadcastsender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACTION_SEND_MESSAGE = "com.rino.broadcastsender.message"
        private const val NAME_MESSAGE = "MESSAGE"
        private const val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000
    }

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendButton.setOnClickListener {
            val intent = Intent().apply {
                action = ACTION_SEND_MESSAGE
                putExtra(NAME_MESSAGE, binding.messageEditText.text.toString())
                addFlags(FLAG_RECEIVER_INCLUDE_BACKGROUND)
            }

            sendBroadcast(intent)
        }
    }
}