package com.example.todoproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import java.util.Date

class Day_adapter(private val month: Int, private val dayList: List<Date>, private val onDateSelected: (String) -> Unit) : RecyclerView.Adapter<Day_adapter.DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = dayList[position]
        holder.bind(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) == month) {
            onDateSelected("${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}")
        }
    }

    override fun getItemCount() = dayList.size

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.item_day_text)

        fun bind(day: Int, isCurrentMonth: Boolean, onClick: () -> Unit) {
            dayTextView.text = day.toString()
            itemView.setOnClickListener { onClick() }
        }
    }
}
