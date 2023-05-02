package com.example.askme.exercise

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.askme.MainActivity
import com.example.askme.R

class ExerciseMain : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_main)

        val cardViewRightAnswer = findViewById<CardView>(R.id.cvRightAnswer)
        val cardViewWrongAnswer = findViewById<CardView>(R.id.cvWrongAnswer)
        val editTextAnswer = findViewById<EditText>(R.id.etEnglishWord)
        val buttonExit = findViewById<Button>(R.id.btnExit)
        val buttonCheckAnswer = findViewById<Button>(R.id.btnCheckButton)

        var enteredAnswer = ""

        buttonExit.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        buttonCheckAnswer.setOnClickListener{
            enteredAnswer = editTextAnswer.text.toString()

            if (enteredAnswer == "A horse"){
                cardViewRightAnswer.visibility = VISIBLE
            }else{
                cardViewWrongAnswer.visibility = VISIBLE
            }
        }

    }
}