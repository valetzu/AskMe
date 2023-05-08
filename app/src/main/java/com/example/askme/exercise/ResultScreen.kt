package com.example.askme.exercise

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.askme.MainActivity
import com.example.askme.R

class ResultScreen : AppCompatActivity() {

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_screen)

        mediaPlayer = MediaPlayer.create(this, R.raw.result)

       val sharedPref = getSharedPreferences("my_sf", Context.MODE_PRIVATE)
       val rightAnswerAmount = sharedPref.getInt("sf_rightAnswer", 0)
       val wrongAnswerAmount = sharedPref.getInt("sf_wrongAnswer", 0)
        val nearlyCorrectAnswerAmount = sharedPref.getInt("sf_nearlyCorrectAnswer", 0)
        val previousPersonalBest = sharedPref.getFloat("sf_personalBest", 0.0f)
        editor = sharedPref.edit()

        val rightAnswers = findViewById<TextView>(R.id.tvRightAnswerAmount)
        val wrongAnswers = findViewById<TextView>(R.id.tvWrongAnswerAmount)
        val nearlyCorrectAnswers = findViewById<TextView>(R.id.tvNearlyCorrectAnswerAmount)
        val resultComplete = findViewById<TextView>(R.id.tvResult)
        val tvPersonalBest = findViewById<TextView>(R.id.tvPersonalBest)
        val returnButton = findViewById<Button>(R.id.btnReturn)
        val reverseButton = findViewById<Button>(R.id.btnReverse)
        val repeatButton = findViewById<Button>(R.id.btnRepeat)

        var answerAmount = rightAnswerAmount + wrongAnswerAmount + nearlyCorrectAnswerAmount
        var finalResult = 100f * (rightAnswerAmount + nearlyCorrectAnswerAmount).toFloat()/answerAmount.toFloat()

        var wasFlipped = intent.getBooleanExtra("WASFLIPPED", false)

        mediaPlayer.start()
        rightAnswers.text = "Oikeita vastauksia $rightAnswerAmount"
        wrongAnswers.text = "Vääriä vastauksia $wrongAnswerAmount"
        nearlyCorrectAnswers.text = "Melkein oikein $nearlyCorrectAnswerAmount"
        resultComplete.text = "Kokonaistulos $finalResult %"
        if(finalResult > previousPersonalBest){
            editor.apply{
                putFloat("sf_personalBest", finalResult)
                commit()
            }
            tvPersonalBest.text = "Oma ennätys $finalResult %"
        } else {
            tvPersonalBest.text = "Oma ennätys $previousPersonalBest %"
        }


        repeatButton.setOnClickListener{
            val intent = Intent(this, ExerciseMain::class.java)
            startActivity(intent)
        }

        reverseButton.setOnClickListener{
            val intent = Intent(this, ExerciseMain::class.java)
            if(!wasFlipped){
                intent.putExtra("FLIPPED",true)
            }else{
                intent.putExtra("FLIPPED", false)
            }
            startActivity(intent)
        }

        returnButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}
