package com.example.askme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonOpenExercise = findViewById<TextView>(R.id.btnOpenExercise)
        val buttonSettings = findViewById<TextView>(R.id.btnSettings)

        //TODO: improve the buttons on start view
        // (connect each icon image and its button and adjust constraints)

        buttonOpenExercise.setOnClickListener {
            //TODO: open the first exercise view
        }

        buttonSettings.setOnClickListener{
            //TODO: open the settings view
        }
    }
}