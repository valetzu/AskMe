package com.example.askme.exercise

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.askme.MainActivity
import com.example.askme.R

class ChooseExercise : AppCompatActivity() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val courseList = listOf<Course>(
        //TODO: fileName is critical! app does not yet check whether it actually exists
        Course("EN1", "Demo course 1 for preview", "eng_exercise1", "english"),
        Course("EN2", "Demo course 2 for preview", "eng_exercise2", "english"),
        Course("SWE1", "Demo course 3 for preview", "swe_exercise1", "swedish"),
        Course("EN3", "Demo course 4 for preview", "eng_exercise3", "english"),
        Course("EN4", "Demo course 5 for preview", "eng_exercise4", "english")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_exercise)

        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()

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
                "This feature is not available yet",
                Toast.LENGTH_SHORT
            ).show()
        }

        createExerciseButton.setOnClickListener{
            Toast.makeText(
                this@ChooseExercise,
                "This feature is not available yet",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
    private fun listItemClicked(course: Course){
            val intent = Intent(this, ExerciseMain::class.java)
            editor.apply{
                putString("sf_coursename", course.name)
                putString("sf_coursedesc", course.description)
                putString("sf_coursefilename", course.fileName)
                putString("sf_courselanguage", course.language)
                commit()
            }
            startActivity(intent)
    }
}