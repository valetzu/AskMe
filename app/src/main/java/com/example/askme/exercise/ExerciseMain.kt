package com.example.askme.exercise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.askme.MainActivity
import com.example.askme.R

class ExerciseMain : AppCompatActivity() {
    var rightAnswerAmount = 0
    var wrongAnswerAmount = 0
    var nearlyCorrectAnswers = 0
    var enteredAnswer = ""
    var courseFileName = "eng_exercise1"
    var courseName = "DefaultValue"
    var courseDesc = "DefaultValue"
    var courseLang = "english"
    private lateinit var sf:SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var wordPairList : MutableList<Pair<String, String>> = mutableListOf()
    private lateinit var mediaPlayerSuccess: MediaPlayer
    private lateinit var mediaPlayerFail: MediaPlayer
    var flippedModeEnabled = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_main)
        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()
        resetValuesInSF()

        wrongAnswerAmount = 0
        rightAnswerAmount = 0
        nearlyCorrectAnswers = 0

        mediaPlayerSuccess = MediaPlayer.create(this, R.raw.success)
        mediaPlayerFail = MediaPlayer.create(this, R.raw.fail)

        val cvRightAnswer = findViewById<CardView>(R.id.cvRightAnswer)!!
        val cvWrongAnswer = findViewById<CardView>(R.id.cvWrongAnswer)!!
        val cvNearlyCorrectAnswer = findViewById<CardView>(R.id.cvNearlyCorrectAnswer)!!
        val etTextAnswer = findViewById<EditText>(R.id.etEnglishWord)!!
        val buttonExit = findViewById<Button>(R.id.btnExit)!!
        val buttonCheckAnswer = findViewById<Button>(R.id.btnCheckButton)!!
        val buttonContinue = findViewById<Button>(R.id.btnContinueButton)
        val wordFinnish = findViewById<TextView>(R.id.tvFinnishWord)
        val tvRedBoxAnswer = findViewById<TextView>(R.id.tvWrongAnswerDescription)
        val tvGreenBoxAnswer = findViewById<TextView>(R.id.tvRightAnswerDescription)
        val tvYellowBoxAnswer = findViewById<TextView>(R.id.tvNearlyCorrectAnswerDescription)
        val tvAskingLanguage = findViewById<TextView>(R.id.tvEnglishLanguage)
        val tvAnsweringLanguage = findViewById<TextView>(R.id.tvFinnishLanguage)
        val tvExerciseProgress = findViewById<TextView>(R.id.tvChooseExercise)

        val tvCourseName = findViewById<TextView>(R.id.tvCourseName)
        val tvCourseDesc = findViewById<TextView>(R.id.tvTopBarDesc)

        courseFileName = sf.getString("sf_coursefilename", "eng_exercise1").toString()
        courseName = sf.getString("sf_coursename", "DefaultValue").toString()
        courseDesc = sf.getString("sf_coursedesc", "DefaultValue").toString()
        courseLang = sf.getString("sf_courselanguage", "DefaultValue").toString()
        val mutedEnabled = sf.getBoolean("sf_muted_enabled", false)

        tvCourseName.text = courseName
        tvCourseDesc.text = courseDesc
        flippedModeEnabled = intent.getBooleanExtra("FLIPPED", false)
        var exerciseProgress = 1
        var wordIndex = 0
        val nearlyCorrectThresholdPercentage = 75.0

        //Depending on the mode, update the guess and answer languages
        if(flippedModeEnabled){
            wordPairList = readExerciseFromResources(courseFileName)
            wordPairList = getListWithFlippedLanguages(wordPairList)
            when(courseLang){
                "english" -> {
                    tvAskingLanguage.text = "suomi"
                    tvAnsweringLanguage.text = "englanti"
                }
                "swedish"-> {
                    tvAskingLanguage.text = "suomi"
                    tvAnsweringLanguage.text = "ruotsi"
                }
            }
        } else {
            wordPairList = readExerciseFromResources(courseFileName)
            when(courseLang){
                "swedish"-> tvAskingLanguage.text = "ruotsi"
            }
        }

        //Update exercise progress
        tvExerciseProgress.text = "$exerciseProgress / ${wordPairList.size}"
        //Update initial words to be displayed
            wordFinnish.text = wordPairList.get(wordIndex).first
        var wordEnglishAnswer = wordPairList.get(wordIndex).second
            buttonCheckAnswer.text = "Tarkasta vastaus"
            buttonContinue.text = "Jatka"

        /**
         * Displays the results of the answer correct percentage.
         * @param answerCorrectPercentage The percentage of correct answers.
         */
        fun displayResults(answerCorrectPercentage: Double) {
            when(answerCorrectPercentage){
                100.0 -> {
                    if(!mutedEnabled){
                        mediaPlayerSuccess.start()
                    }
                    rightAnswerAmount++
                    tvGreenBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cvRightAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
                in nearlyCorrectThresholdPercentage..99.9 -> {
                    if(!mutedEnabled){
                        mediaPlayerSuccess.start()
                    }
                    nearlyCorrectAnswers++
                    tvYellowBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cvNearlyCorrectAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
                else -> {
                    if(!mutedEnabled){
                        mediaPlayerFail.start()
                    }
                    wrongAnswerAmount++
                    tvRedBoxAnswer.text = wordPairList.get(wordIndex).second
                    buttonCheckAnswer.visibility = INVISIBLE
                    cvWrongAnswer.visibility = VISIBLE
                    buttonContinue.visibility = VISIBLE
                }
            }
        }

        buttonExit.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        buttonCheckAnswer.setOnClickListener{
                enteredAnswer = etTextAnswer.text.toString()
                // Stops user from modifying the answer
                etTextAnswer.setEnabled(false)
                //
                displayResults(checkAnswer(enteredAnswer, wordEnglishAnswer))
        }

        buttonContinue.setOnClickListener{
            // Resets editText
            etTextAnswer.setEnabled(true)
            //
            cvRightAnswer.visibility = INVISIBLE
            cvNearlyCorrectAnswer.visibility = INVISIBLE
            cvWrongAnswer.visibility = INVISIBLE
            etTextAnswer.text.clear()
            wordIndex++
            if(wordIndex < wordPairList.size){
                exerciseProgress++
                tvExerciseProgress.text = "$exerciseProgress / ${wordPairList.size}"
                buttonContinue.visibility = INVISIBLE
                buttonCheckAnswer.visibility = VISIBLE
                wordFinnish.text = wordPairList.get(wordIndex).first
                wordEnglishAnswer = wordPairList.get(wordIndex).second
            } else {
                results()
            }
        }

        /**
         * Changes the enter key to a send key on the keyboard. Acts as the check answer button in this context.
         */
        //Send button
        fun sendMessage() {
            hideKeyboard(this)
            enteredAnswer = etTextAnswer.text.toString()
            // Stops user from modifying the answer
            etTextAnswer.setEnabled(false)
            //
            displayResults(checkAnswer(enteredAnswer, wordEnglishAnswer))
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


    /**
     * Hides the keyboard, when "send" button on the keyboard is pressed.
     */
    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Completes the exercise completion activities
     */
    private fun results(){
        saveValuesToSF()
        val intent = Intent(this, ResultScreen::class.java)
        if (flippedModeEnabled){
            intent.putExtra("WASFLIPPED", true)
        }
        startActivity(intent)
    }
    override fun onPause() {
        super.onPause()
        saveValuesToSF()
    }

    override fun onDestroy(){
        super.onDestroy()
        resetValuesInSF()
    }
    override fun onResume(){
        super.onResume()
        getValuesFromSF()
    }

    /**
     * Saves the values to shared preferences
     */
    fun saveValuesToSF(){
        editor.apply{
            putInt("sf_rightAnswer", rightAnswerAmount)
            putInt("sf_wrongAnswer", wrongAnswerAmount)
            putInt("sf_nearlyCorrectAnswer", nearlyCorrectAnswers)
            commit()
        }
    }
    /**
     * Resets the values in shared preferences.
     */
    fun resetValuesInSF(){
        editor.apply{
            putInt("sf_rightAnswer", 0)
            putInt("sf_wrongAnswer", 0)
            putInt("sf_nearlyCorrectAnswer", 0)
            commit()
        }
    }

    /**
     * Reads the values from shared preferences.
     */
    fun getValuesFromSF(){
        wrongAnswerAmount = sf.getInt("sf_rightAnswer", 0)
        rightAnswerAmount = sf.getInt("sf_wrongAnswer", 0)
        nearlyCorrectAnswers = sf.getInt("sf_nearlyCorrectAnswer", 0)
        courseFileName = sf.getString("sf_coursefilename", "eng_exercise1").toString()
        courseName = sf.getString("sf_coursename", "eng_exercise1").toString()
        courseDesc = sf.getString("sf_coursedesc", "eng_exercise1").toString()
    }

    /**
     * Reads an exercise from resources.
     * @param courseFileName The name of the course file.
     * @return The list of pairs.
     */
    fun readExerciseFromResources(courseFileName : String) : MutableList<Pair<String, String>>{
        val resourceId = getResources().getIdentifier(courseFileName, "array", packageName)
        val tempWordPairList : MutableList<Pair<String, String>> = mutableListOf()
        val array = resources.getStringArray(resourceId)
        array.forEach{
            val pairAsArray = it.split(',')
            val pair = Pair<String, String>(pairAsArray[0], pairAsArray[1])
            tempWordPairList.add(pair)
        }
        return tempWordPairList
    }

    /**
     * Returns a list with the languages flipped.
     * @param listToFlip The list to flip.
     * @return The flipped list. (ie. before: pair FIN:ENG, after: ENG:FIN or vice versa)
     */
    fun getListWithFlippedLanguages(listToFlip : MutableList<Pair<String,String>>) : MutableList<Pair<String,String>>{
        val tempWordPairList : MutableList<Pair<String, String>> = mutableListOf()
        listToFlip.forEach{
            val flippedPair = Pair<String, String>(it.second, it.first)
            tempWordPairList.add(flippedPair)
        }
        return tempWordPairList
    }


    /**
     * Determines a proper comparison treatment for the guess given.
     * Returns the % of similarity to the given answer.
     * Takes into account the number of same character occurences
     * and also the number of same characters in a correct spot.
     * @param guess The string that contains the guess.
     * @param answer The string that contains the correct answer.
     * @return The percentage of similarity to the given answer.
     */
    fun checkAnswer(guess : String, answer : String) : Double{
        var correctPercentage: Double = 0.0
        val thresholdGuessLengthToInstaFail = 2

        if(guess == answer){
            return 100.0
        }

        //Give instafail if guess length exceeds given threshold times actual answer length.
        if(guess.length >= thresholdGuessLengthToInstaFail * answer.length){
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
            val guessAndArticle = guess.split(' ')
            val guessArticle = guessAndArticle[0]
            val guessWithoutArticle = guessAndArticle[1]
            //Split the guess word apart from its article
            if (guessArticle.length <= 2){
                if(guessArticle == answerArticle){
                    correctPercentage = checkTranslation(guess, answer)
                } else {
                    if(answerWithoutArticle in guess) {
                        //the word is correct except it is missing the article
                        when(guess.length){
                            in answerWithoutArticle.length..answerWithoutArticle.length+2 -> {
                                return 99.0
                            }
                            else -> {
                                return 0.0
                            }
                        }
                    }
                    correctPercentage = checkTranslation(guessWithoutArticle, answerWithoutArticle)
                }
            } else {
                correctPercentage = checkTranslation(guess, answerWithoutArticle)
            }
        }  else {
            if(answerWithoutArticle in guess) {
                //the word is correct except it is missing the article
                when(guess.length){
                    in answerWithoutArticle.length..answerWithoutArticle.length+2 -> {
                        return 99.0
                    }
                    else -> {
                        return 0.0
                    }
                }
            }
            correctPercentage = checkTranslation(guess, answerWithoutArticle)
        }
        return correctPercentage
    }

    /**
     * * Does the actual comparison between given word and the answer.
     * Takes into account the number of same character occurences
     * and also the number of same characters in a correct spot
     * Returns the percentage of correct characters in the guess string.
     * @param guess The string that contains the guess.
     * @param answer The string that contains the correct answer.
     * @return The percentage of correct characters in the guess string (similarity%).
     */
    fun checkTranslation(guess : String, answer : String) : Double{
        var correctPercentage: Double = 0.0
        //the number of correct characters at correct spot
        var correctCharCount = 0
        val maxCorrectCharCount = answer.length
        //
        //The number of same character occurences
        var sameCharsFound = 0
        var checkedChars : MutableList<Char> = mutableListOf()
        var sameCharsPercentage = 0.0
        //
        for (i in guess.indices) {
            if (guess[i] in answer && checkedChars.count { it == guess[i] } < answer.count { it == guess[i] }) {
                sameCharsFound++
                checkedChars.add(guess[i])
            }
            if (i >= answer.length) {
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
        sameCharsPercentage = (sameCharsFound.toDouble() / answer.length.toDouble()) * 100.0
        correctPercentage = (correctPercentage + sameCharsPercentage) / 2.0
        return correctPercentage
    }
}