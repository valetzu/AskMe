package com.example.askme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.askme.exercise.ExerciseMain

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonOpenExercise = findViewById<Button>(R.id.btnOpenExercise)
        val buttonSettings = findViewById<Button>(R.id.btnSettings)
        val buttonNewExercise = findViewById<Button>(R.id.btnNewExercise)

        //TODO: improve the buttons on start view
        // (connect each icon image and its button and adjust constraints)

        buttonOpenExercise.setOnClickListener {
            val intent = Intent(this, ExerciseMain::class.java)
            startActivity(intent)
        }

        buttonSettings.setOnClickListener{
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }

        buttonNewExercise.setOnClickListener{
            Toast.makeText(this@MainActivity,
                "Feature is currently locked. Coming soon!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}