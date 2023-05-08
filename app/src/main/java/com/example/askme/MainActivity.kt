package com.example.askme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.askme.exercise.ChooseExercise
import com.example.askme.settings.SettingsScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonOpenExercise = findViewById<Button>(R.id.btnOpenExercise)
        val buttonSettings = findViewById<Button>(R.id.btnSettings)
        val buttonNewExercise = findViewById<Button>(R.id.btnNewExercise)

        buttonOpenExercise.setOnClickListener {
            val intent = Intent(this, ChooseExercise::class.java)
            startActivity(intent)
        }

        buttonSettings.setOnClickListener{
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }

        buttonNewExercise.setOnClickListener{
            Toast.makeText(this@MainActivity,
                "This feature is not available yet",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}