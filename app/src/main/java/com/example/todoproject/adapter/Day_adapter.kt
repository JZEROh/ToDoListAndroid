package com.example.todoproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Day_adapter(
    val tempMonth: Int,
    val dayList: MutableList<Date>,
    private val dateSelectedCallback: (String) -> Unit // 날짜 선택 콜백 추가
) : RecyclerView.Adapter<Day_adapter.DayView>() {
    val ROW = 6

    class DayView(val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return DayView(view)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        // 초기화
        val day_text: TextView = holder.layout.findViewById(R.id.item_day_text)

        // 날짜 표시
        day_text.text = dayList[position].date.toString()
        if (tempMonth != dayList[position].month) {
            day_text.alpha = 0.4f
        }

        // 토요일이면 파란색, 일요일이면 빨간색으로 색상 표시
        if ((position + 1) % 7 == 0) {
            day_text.setTextColor(ContextCompat.getColor(holder.layout.context, R.color.blue))
        } else if (position == 0 || position % 7 == 0) {
            day_text.setTextColor(ContextCompat.getColor(holder.layout.context, R.color.red))
        }

        // 날짜 클릭 시 콜백을 통해 선택된 날짜 전달
        holder.layout.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = dateFormat.format(dayList[position])
            dateSelectedCallback(selectedDate) // 콜백 호출
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
