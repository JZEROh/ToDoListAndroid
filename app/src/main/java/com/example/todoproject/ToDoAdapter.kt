package com.example.todoproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ToDoAdapter(val toDoItems: List<String>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTextView: TextView = itemView.findViewById(R.id.toDoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item,parent,false)
        return ToDoViewHolder(itemView)
    }

    override fun getItemCount() = toDoItems.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.todoTextView.text = toDoItems[position]
    }
}
