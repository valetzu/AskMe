package com.example.askme.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.askme.MainActivity
import com.example.askme.R

class InfoScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_info)

        val buttonExit = findViewById<Button>(R.id.btnExit)
        buttonExit.setOnClickListener{
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }
    }
}