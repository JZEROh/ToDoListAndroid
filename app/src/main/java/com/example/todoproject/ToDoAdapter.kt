package com.example.todoproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(private val itemList: List<ToDoItem>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val btnAdjust: Button = itemView.findViewById(R.id.btnadjust)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvTitle.text = item.title
        holder.tvDetail.text = item.detail
        holder.tvDate.text = item.date

        // Handle button click events if needed
        holder.btnAdjust.setOnClickListener {
            // Handle adjust button click
        }

        holder.btnDelete.setOnClickListener {
            // Handle delete button click
        }
    }

    override fun getItemCount() = itemList.size
}
