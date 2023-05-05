package com.example.askme.exercise

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.askme.MainActivity
import com.example.askme.R

class ExerciseMain : AppCompatActivity() {

    private var questionNumber = 0
    private var questionArray = arrayOf<String>("hevonen","a horse","lehmä","a cow","lammas","a sheep")

    var rightAnswerAmount = 0
    var wrongAnswerAmount = 0
    var enteredAnswer = ""
    private lateinit var sf:SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_main)

        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()

        questionNumber = 0
        wrongAnswerAmount = 0
        rightAnswerAmount = 0

        val cardViewRightAnswer = findViewById<CardView>(R.id.cvRightAnswer)!!
        val cardViewWrongAnswer = findViewById<CardView>(R.id.cvWrongAnswer)!!
        val editTextAnswer = findViewById<EditText>(R.id.etEnglishWord)!!
        val buttonExit = findViewById<Button>(R.id.btnExit)!!
        val buttonCheckAnswer = findViewById<Button>(R.id.btnCheckButton)!!
        val buttonContinue = findViewById<Button>(R.id.btnContinueButton)
        val wordFinnish = findViewById<TextView>(R.id.tvFinnishWord)
        val realWordWrong = findViewById<TextView>(R.id.tvWrongAnswerDescription)
        val realWordRight = findViewById<TextView>(R.id.tvRightAnswerDescription)

        buttonExit.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        wordFinnish.text = questionArray[questionNumber]
        buttonCheckAnswer.text = "Tarkasta vastaus"
        buttonContinue.text = "Jatka"

        buttonCheckAnswer.setOnClickListener{
            enteredAnswer = editTextAnswer.text.toString()
            if (enteredAnswer == questionArray[questionNumber+1]){
                rightAnswerAmount += 1
                realWordRight.text = questionArray[questionNumber+1]
                cardViewRightAnswer.visibility = VISIBLE
                rightAnswerAmount += rightAnswerAmount
                buttonCheckAnswer.visibility = INVISIBLE
                buttonContinue.visibility = VISIBLE

            }else{
                wrongAnswerAmount += 1
                realWordWrong.text = questionArray[questionNumber+1]
                cardViewWrongAnswer.visibility = VISIBLE
                wrongAnswerAmount += wrongAnswerAmount
                buttonCheckAnswer.visibility = INVISIBLE
                buttonContinue.visibility = VISIBLE
            }
        }

        buttonContinue.setOnClickListener{
            questionNumber += 2
            cardViewRightAnswer.visibility = INVISIBLE
            cardViewWrongAnswer.visibility = INVISIBLE
            editTextAnswer.text.clear()

            if(questionNumber < questionArray.size){
                buttonContinue.visibility = INVISIBLE
                buttonCheckAnswer.visibility = VISIBLE
                wordFinnish.text = questionArray[questionNumber]
            }else{
                results()
            }
        }
    }

    private fun results(){
        val intent = Intent(this, ResultScreen::class.java)
        intent.putExtra("WRONGANSWERAMOUNT", wrongAnswerAmount)
        intent.putExtra("RIGHTANSWERAMOUNT", rightAnswerAmount)
        startActivity(intent)
    }
    override fun onPause() {
        super.onPause()
        val rAnswer = rightAnswerAmount.toInt()
        val wAnswer = wrongAnswerAmount.toInt()
        editor.apply{
            putInt("sf_rightAnswer", rAnswer)
            putInt("sf_wrongAnswer", wAnswer)
            commit()
        }
    }
    override fun onResume(){
        super.onResume()
        val resumeRightAnswer = sf.getInt("sf_rightAnswer", 0)
        val resumeWrongAnswer = sf.getInt("sf_wrongAnswer", 0)
        wrongAnswerAmount = resumeWrongAnswer
        rightAnswerAmount = resumeRightAnswer
    }
}