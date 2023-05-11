package com.example.askme.exercise

import androidx.lifecycle.ViewModel

class ExerciseMainViewModel : ViewModel() {
    var wrongAnswers = 0
    var rightAnswers = 0
    var nearlyCorrectAnswers = 0
    //var totalWordsGuessed = 0

    fun increaseRightAnswers(){
        rightAnswers++
    }

    fun increaseWrongAnswers(){
        wrongAnswers++
    }

    fun increaseNearlyCorrectAnswers(){
        nearlyCorrectAnswers++
    }

    /*fun setTotalWordsGuessed(totalExerciseWords : Int){
        totalWordsGuessed = totalExerciseWords
    }*/
}

