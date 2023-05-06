package com.example.askme.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.askme.R

class RecyclerViewAdapter(private val courseList:List<Course>, private val clickListener:(Course)->Unit) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course,clickListener)
    }
}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    fun bind(course: Course, clickListener: (Course) -> Unit){
        val itemButton = view.findViewById<TextView>(R.id.tvtItem)
        itemButton.text = course.name

        view.setOnClickListener{
            clickListener(course)
        }
    }
}