package com.example.todoproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.R
import com.example.todoproject.data.Todo

data class ToDoAdapter(val toDoItems: List<Todo>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTextView: TextView = itemView.findViewById(R.id.toDoItem)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item,parent,false)
        return ToDoViewHolder(itemView)
    }

    override fun getItemCount() = toDoItems.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todoItem = toDoItems[position]
        holder.todoTextView.text = todoItem.title
        holder.dateTextView.text = todoItem.date
        holder.contentTextView.text = todoItem.content
    }
}
