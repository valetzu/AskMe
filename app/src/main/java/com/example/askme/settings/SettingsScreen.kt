package com.example.askme.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.askme.MainActivity
import com.example.askme.R

class SettingsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen)

        val buttonExit = findViewById<Button>(R.id.btnExit)
        val buttonInfo = findViewById<Button>(R.id.btnInfo)
        val buttonHelp = findViewById<Button>(R.id.btnHelp)

        buttonExit.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonInfo.setOnClickListener{
            val intent = Intent(this, InfoScreen::class.java)
            startActivity(intent)
        }

        buttonHelp.setOnClickListener{
            val intent = Intent(this, HelpScreen::class.java)
            startActivity(intent)
        }





    }
}