package com.example.askme.settings

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import com.example.askme.MainActivity
import com.example.askme.R

class SettingsScreen : AppCompatActivity() {

    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen)
        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()

        var darkmodeEnabled = sf.getBoolean("sf_darkmode_enabled", false)
        var mutedEnabled = sf.getBoolean("sf_muted_enabled", false)
        val buttonExit = findViewById<Button>(R.id.btnExit)
        val buttonInfo = findViewById<Button>(R.id.btnInfo)
        val buttonHelp = findViewById<Button>(R.id.btnHelp)
        val muteSwitch = findViewById<Switch>(R.id.swEnableSound)

        muteSwitch.isChecked = mutedEnabled

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

        muteSwitch?.setOnCheckedChangeListener({ _ , isChecked ->
            val message = if (isChecked) "Sound Muted" else "Sound Unmuted"
            Toast.makeText(this@SettingsScreen, message,
                Toast.LENGTH_SHORT).show()
            editor.apply{
                putBoolean("sf_muted_enabled", isChecked)
                commit()
            }
        })
    }
}