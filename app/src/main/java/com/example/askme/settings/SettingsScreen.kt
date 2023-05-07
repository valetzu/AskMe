package com.example.askme.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import com.example.askme.MainActivity
import com.example.askme.R

class SettingsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen)

        val buttonExit = findViewById<Button>(R.id.btnExit)
        val buttonInfo = findViewById<Button>(R.id.btnInfo)
        val buttonHelp = findViewById<Button>(R.id.btnHelp)
        val darkModeSwitch = findViewById<Switch>(R.id.swEnableDarkMode)
        val muteSwitch = findViewById<Switch>(R.id.swEnableSound)

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

        darkModeSwitch?.setOnCheckedChangeListener({ _ , isChecked ->
            val message = if (isChecked) "Dark mode:ON" else "Dark mode:OFF"
            Toast.makeText(this@SettingsScreen, message,
            Toast.LENGTH_SHORT).show()
        })

        muteSwitch?.setOnCheckedChangeListener({ _ , isChecked ->
            val message = if (isChecked) "Muted" else "Unmuted"
            Toast.makeText(this@SettingsScreen, message,
                Toast.LENGTH_SHORT).show()
        })
    }
}