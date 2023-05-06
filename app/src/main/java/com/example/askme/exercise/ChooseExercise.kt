package com.example.askme.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.askme.MainActivity
import com.example.askme.R

class ChooseExercise : AppCompatActivity() {

    private val courseList = listOf<Course>(
        Course("En 1"),
        Course("En 2"),
        Course("Swe 3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_exercise)

        val exitButton = findViewById<Button>(R.id.btnExit)
        val uploadExerciseButton = findViewById<Button>(R.id.btnUploadExercise)
        val createExerciseButton = findViewById<Button>(R.id.btnCreateNewExercise)
        val recyclerView = findViewById<RecyclerView>(R.id.rvExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = RecyclerViewAdapter(courseList) {selectedItem: Course ->
            listItemClicked(selectedItem)
        }

        exitButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        uploadExerciseButton.setOnClickListener{
            Toast.makeText(
                this@ChooseExercise,
                "Not available yet",
                Toast.LENGTH_SHORT
            ).show()
        }

        createExerciseButton.setOnClickListener{
            Toast.makeText(
                this@ChooseExercise,
                "Not available yet",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun listItemClicked(course: Course){

        if (course.name == "En 1"){
            val intent = Intent(this, ExerciseMain::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(
                this@ChooseExercise,
                "This exercise is not available yet",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}