package com.rino.homework01

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickMeButton = findViewById<Button>(R.id.click_me_button)
        val infoTextView = findViewById<TextView>(R.id.info_text_view)

        clickMeButton.setOnClickListener {
            infoTextView.text = getString(R.string.button_was_clicked)
        }

        val person = Person("Ivan", "Ivanov")
        val personTextView = findViewById<TextView>(R.id.person_text_view)
        personTextView.text = person.toString()

        val personCopy = person.copy()
        val personCopyTextView = findViewById<TextView>(R.id.person_copy_text_view)
        personCopyTextView.text = personCopy.toString()

        Log.d(TAG, " for (i in 1..10) ")
        for (i in 1..10) {
            Log.d(TAG, i.toString())
        }

        Log.d(TAG, " for (1 until 10) ")
        for (i in 1 until 10) {
            Log.d(TAG, i.toString())
        }

        Log.d(TAG, " for (i in 10 downTo 1 step 3) ")
        for (i in 10 downTo 1 step 3) {
            Log.d(TAG, i.toString())
        }

        Log.d(TAG, "List, size=20 => .forEach { } ")
        val testArray = List(10) { it * 2 }
        testArray.forEach { Log.d(TAG, it.toString()) }
    }

}