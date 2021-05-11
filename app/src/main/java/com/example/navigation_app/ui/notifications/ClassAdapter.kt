package com.example.navigation_app.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation_app.R

class ClassAdapter(private val classContent: MutableList<ClassContent>) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val class_name : TextView = itemView.findViewById(R.id.class_name)
        val class_details : TextView = itemView.findViewById(R.id.class_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = classContent[position]
        holder.class_name.text = pos.name
        holder.class_details.text = pos.content
    }

    override fun getItemCount(): Int {
        return classContent.size
    }

}
