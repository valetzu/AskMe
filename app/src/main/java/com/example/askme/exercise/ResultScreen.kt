package com.example.askme.exercise

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.askme.MainActivity
import com.example.askme.R

class ResultScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_screen)

        val rightAnswers = findViewById<TextView>(R.id.tvRightAnswerAmount)
        val wrongAnswers = findViewById<TextView>(R.id.tvWrongAnswerAmount)
        val resultComplete = findViewById<TextView>(R.id.tvResult)
        val returnButton = findViewById<Button>(R.id.btnReturn)
        val wrongAnswerAmount = intent.getIntExtra("WRONGANSWERAMOUNT", 0)
        val rightAnswerAmount = intent.getIntExtra("RIGHTANSWERAMOUNT", 0)
        var answerAmount = rightAnswerAmount + wrongAnswerAmount

        var finalResult ="$rightAnswerAmount/$answerAmount"

        rightAnswers.text = "Oikeita vastauksia " + rightAnswerAmount
        wrongAnswers.text = "Vääriä vastauksia " + wrongAnswerAmount
        resultComplete.text = "Kokonaistulos " + finalResult

        returnButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        }
    }