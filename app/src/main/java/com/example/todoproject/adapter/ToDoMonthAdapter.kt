package com.example.todoproject.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.R
import com.example.todoproject.data.ToDoItem
import java.text.SimpleDateFormat
import java.util.Locale


class ToDoMonthAdapter(private val context: Context, private val itemList: List<ToDoItem>) : RecyclerView.Adapter<ToDoMonthAdapter.ToDoViewHolder>() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
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
        holder.tvDate.text = dateFormat.format(item.date)

        // Handle button click events if needed
        holder.btnAdjust.setOnClickListener {
            showEditDialog(position)
        }

        holder.btnDelete.setOnClickListener {
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount() = itemList.size

    private fun showEditDialog(position: Int) {
        val item = itemList[position]

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_item, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etDetail = dialogView.findViewById<EditText>(R.id.etDetail)
        val etDate = dialogView.findViewById<EditText>(R.id.etDate)

        etTitle.setText(item.title)
        etDetail.setText(item.detail)
        etDate.setText(dateFormat.format(item.date))

        AlertDialog.Builder(context)
            .setTitle("Edit ToDo Item")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                item.title = etTitle.text.toString()
                item.detail = etDetail.text.toString()
                try {
                    item.date = dateFormat.parse(etDate.text.toString()) ?: item.date
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                notifyItemChanged(position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}