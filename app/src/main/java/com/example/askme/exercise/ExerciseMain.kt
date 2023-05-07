package com.example.askme.exercise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.askme.MainActivity
import com.example.askme.R

class ExerciseMain : AppCompatActivity() {

    var rightAnswerAmount = 0
    var wrongAnswerAmount = 0
    var nearlyCorrectAnswers = 0
    var enteredAnswer = ""
    private lateinit var sf:SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var wordPairList : MutableList<Pair<String, String>> = mutableListOf()
    var wordIndex = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_main)

        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()
        wrongAnswerAmount = 0
        rightAnswerAmount = 0
        nearlyCorrectAnswers = 0


        val cardViewRightAnswer = findViewById<CardView>(R.id.cvRightAnswer)!!
        val cardViewWrongAnswer = findViewById<CardView>(R.id.cvWrongAnswer)!!
        val cardViewNearlyCorrectAnswer = findViewById<CardView>(R.id.cvNearlyCorrectAnswer)!!
        val editTextAnswer = findViewById<EditText>(R.id.etEnglishWord)!!
        val buttonExit = findViewById<Button>(R.id.btnExit)!!
        val buttonCheckAnswer = findViewById<Button>(R.id.btnCheckButton)!!
        val buttonContinue = findViewById<Button>(R.id.btnContinueButton)
        val wordFinnish = findViewById<TextView>(R.id.tvFinnishWord)


        //
        val tvRedBoxAnswer = findViewById<TextView>(R.id.tvWrongAnswerDescription)
        val tvGreenBoxAnswer = findViewById<TextView>(R.id.tvRightAnswerDescription)
        val tvYellowBoxAnswer = findViewById<TextView>(R.id.tvNearlyCorrectAnswerDescription)
        buttonExit.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        var wordIndex = 0
        val nearlyCorrectThresholdPercentage = 75.0
           wordPairList = readExerciseFromResources()
            wordFinnish.text = wordPairList.get(wordIndex).first
        var wordEnglishAnswer = wordPairList.get(wordIndex).second
            buttonCheckAnswer.text = "Tarkasta vastaus"
            buttonContinue.text = "Jatka"
        buttonCheckAnswer.setOnClickListener{
            enteredAnswer = editTextAnswer.text.toString()
            var percentageCorrect = checkAnswer(enteredAnswer, wordEnglishAnswer)
            when(percentageCorrect){
                100.0 -> {
                    rightAnswerAmount++
                    tvGreenBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cardViewRightAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
                in nearlyCorrectThresholdPercentage..99.9 -> {
                    nearlyCorrectAnswers++
                    tvYellowBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cardViewNearlyCorrectAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
                else -> {
                    wrongAnswerAmount++
                    tvRedBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cardViewWrongAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
            }
        }

        buttonContinue.setOnClickListener{
            cardViewRightAnswer.visibility = INVISIBLE
            cardViewNearlyCorrectAnswer.visibility = INVISIBLE
            cardViewWrongAnswer.visibility = INVISIBLE
            editTextAnswer.text.clear()
            wordIndex++
            if(wordIndex < wordPairList.size){
                buttonContinue.visibility = INVISIBLE
                buttonCheckAnswer.visibility = VISIBLE
                wordFinnish.text = wordPairList.get(wordIndex).first
                wordEnglishAnswer = wordPairList.get(wordIndex).second
            } else {
                results()
            }
        }

        //Send button

        fun sendMessage() {
            hideKeyboard(this)
            enteredAnswer = editTextAnswer.text.toString()
            editTextAnswer.text.clear()
            var percentageCorrect = checkAnswer(enteredAnswer, wordEnglishAnswer)
            when(percentageCorrect){
                100.0 -> {
                    rightAnswerAmount++
                    tvGreenBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cardViewRightAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
                in nearlyCorrectThresholdPercentage..99.9 -> {
                    nearlyCorrectAnswers++
                    tvYellowBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cardViewNearlyCorrectAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
                else -> {
                    wrongAnswerAmount++
                    tvRedBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cardViewWrongAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
            }
        }

        findViewById<EditText>(R.id.etEnglishWord).setOnEditorActionListener {v, etEnglishWord, event ->
            return@setOnEditorActionListener when (etEnglishWord) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }

        //Send button ends
    }

    //Hides keyboard
    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //

    private fun results(){
        val rAnswer = rightAnswerAmount.toInt()
        val wAnswer = wrongAnswerAmount.toInt()
        editor.apply{
            putInt("sf_rightAnswer", rAnswer)
            putInt("sf_wrongAnswer", wAnswer)
            commit()
        }
        val intent = Intent(this, ResultScreen::class.java)
        /*intent.putExtra("RIGHTANSWERAMOUNT", rightAnswerAmount)*/
        /*intent.putExtra("WRONGANSWERAMOUNT", wrongAnswerAmount)*/
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
    fun readExerciseFromResources() : MutableList<Pair<String, String>>{
        //TODO implement exercise file name parameter to be used in this function
        // currently hardcoded resource as "eng_exercise1"
        val resources = getResources()
        val tempWordPairList : MutableList<Pair<String, String>> = mutableListOf()
        val array = resources.getStringArray(R.array.eng_exercise1)
        array.forEach{
            val pairAsArray = it.split(',')
            val pair = Pair<String, String>(pairAsArray[0], pairAsArray[1])
            Log.d("test", "${pair}")
            tempWordPairList.add(pair)
        }
        return tempWordPairList
    }

    //Takes: Mutable string pair list as parameter
    //Returns: same list with flipped word pairs (before: FIN:ENG, after: ENG:FIN or vice versa)
    fun getListWithFlippedLanguages(listToFlip : MutableList<Pair<String,String>>) : MutableList<Pair<String,String>>{
        val tempWordPairList : MutableList<Pair<String, String>> = mutableListOf()
        listToFlip.forEach{
            val flippedPair = Pair<String, String>(it.second, it.first)
            tempWordPairList.add(flippedPair)
        }
        return tempWordPairList
    }

    //Determines a proper comparison treatment for the guess given.
    //Returns the % of similarity to the given answer
    //Takes into account the number of same character occurences
    // and also the number of same characters in a correct spot
    fun checkAnswer(guess : String, answer : String) : Double{
        var correctPercentage: Double = 0.0
        Log.d("test", "Guess: $guess, Answer: $answer")
        val thresholdGuessLengthToInstaFail = 2

        if(guess == answer){
            Log.d("test", "Guess is completely correct!")
            return 100.0
        }

        //Give instafail if guess length exceeds given threshold times actual answer length.
        if(guess.length >= thresholdGuessLengthToInstaFail * answer.length){
            Log.d("test", "What a shitty try. don't just spam random poop")
            return 0.0
        }
        var answerArticle = ""
        var answerWithoutArticle = answer
        //Split the answer and its article apart for further treatment
        if(' ' in answer) {
            val answerAndArticle = answer.split(' ')
            answerArticle = answerAndArticle[0]
            answerWithoutArticle = answerAndArticle[1]
        }

        //If the guess has a blank space, treat it as a word with an article in front
        if(" " in guess){
            var guessAndArticle = guess.split(' ')
            var guessArticle = guessAndArticle[0]
            var guessWithoutArticle = guessAndArticle[1]
            Log.d("test", "Guess has a blank space.")

            //Split the guess word apart from its article

            if (guessArticle.length <= 2){
                Log.d("test", "Word has an article with length <= 2")
                if(guessArticle == answerArticle){
                    Log.d("test", "The article is the correct one")
                    correctPercentage = checkTranslation(guess, answer)

                } else {
                    if(answerWithoutArticle in guess) {
                        //the word is correct except it is missing the article
                        Log.d("test", "the word is correct except the article is not the correct one, but close")
                        when(guess.length){
                            in answerWithoutArticle.length..answerWithoutArticle.length+2 -> {
                                return 99.0
                            }
                            else -> {
                                return 0.0
                            }
                        }
                    }
                    Log.d("test", "The article is not the correct one, but close")
                    correctPercentage = checkTranslation(guessWithoutArticle, answerWithoutArticle)

                }

            } else {
                Log.d("test", "The article has over 2 length, assume it's actually the word or just gibberish")
                correctPercentage = checkTranslation(guess, answerWithoutArticle)

            }
        }  else {
            Log.d("test", "The guess has no blank space")

            if(answerWithoutArticle in guess) {
                //the word is correct except it is missing the article
                Log.d("test", "the word is correct except it is missing the article")
                when(guess.length){
                    in answerWithoutArticle.length..answerWithoutArticle.length+2 -> {
                        Log.d("test", "word is max 2 off from answer length")
                        return 99.0
                    }
                    else -> {
                        Log.d("test", "word is over 2 longer than answer")
                        return 0.0
                    }
                }
            }
            correctPercentage = checkTranslation(guess, answerWithoutArticle)

        }

    Log.d("test", "In total, the Word was $correctPercentage % Correct")
        return correctPercentage
    }

    //Does the actual comparison between given word and the answer, returns the similarity %
    //Takes into account the number of same character occurences
    // and also the number of same characters in a correct spot
    fun checkTranslation(guess : String, answer : String) : Double{
        var correctPercentage: Double = 0.0
        Log.d("test", "Guess: $guess, Answer: $answer")

        //the number of correct characters at correct spot
        var correctCharCount = 0
        var maxCorrectCharCount = answer.length
        //
        //The number of same character occurences
        var sameCharsFound = 0
        var checkedChars : MutableList<Char> = mutableListOf()
        var sameCharsPercentage = 0.0
        //
        for (i in guess.indices) {
            if (guess[i] in answer && checkedChars.count { it == guess[i] } < answer.count { it == guess[i] }) {
                sameCharsFound++
                Log.d("testi", "lisayksen jalkeen: $sameCharsFound")
                checkedChars.add(guess[i])
            }
            if (i >= answer.length) {
                //correctCharCount--
                sameCharsFound--
                if (sameCharsFound < 0){
                    break
                }
            } else {
                //Each correct character at correct spot awards a point
                if (guess[i] == answer[i]) {
                    correctCharCount++
                }
            }
        }
        correctPercentage = 100 * (correctCharCount.toDouble() / maxCorrectCharCount.toDouble())
        Log.d("test", "Word was $correctPercentage % Correct")
        Log.d("testi", "matchingChars: $checkedChars")
        sameCharsPercentage = (sameCharsFound.toDouble() / answer.length.toDouble()) * 100.0
        correctPercentage = (correctPercentage + sameCharsPercentage) / 2.0
        return correctPercentage
    }

}