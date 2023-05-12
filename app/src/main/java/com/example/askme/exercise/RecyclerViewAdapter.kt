package com.example.askme.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.askme.R

class RecyclerViewAdapter(private val courseList:List<Course>, private val clickListener:(Course)->Unit) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(listItem)
    }
/**
 * Returns the amount of the courses currently in the list
 * @return Int size of the list
 * */
    override fun getItemCount(): Int {
        return courseList.size
    }

/**
 * OnBindViewHolderFunction
 * Binds the variables of the course object into the view.
 * @param holder The viewholder object into what the binding happens
 * @param position The index of the object being bound
 * */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course,clickListener)
    }
}

/**
 * The viewholder-class for the recyclerview
 */
class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    fun bind(course: Course, clickListener: (Course) -> Unit){
        val itemButton = view.findViewById<TextView>(R.id.tvItem)
        val itemDesc = view.findViewById<TextView>(R.id.tvCourseDesc)
        itemButton.text = course.name
        itemDesc.text = course.description

        view.setOnClickListener{
            clickListener(course)
        }
    }
}