package com.example.todoproject

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(supportFragmentManager, generateMonthlyData())
        viewPager.adapter = adapter
    }
    private fun generateMonthlyData(): Map<String, List<ToDoItem>> {
        // 예제 데이터를 생성하는 함수입니다.
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val itemList = listOf(
            ToDoItem("제목1", "설명1", dateFormat.parse("20240203") ?: Date()),
            ToDoItem("제목2", "설명2", dateFormat.parse("20240201") ?: Date()),
            ToDoItem("제목3", "설명3", dateFormat.parse("20240206") ?: Date()),
            ToDoItem("제목4", "설명4", dateFormat.parse("20240401") ?: Date())
        )

        // 월별로 데이터를 그룹화합니다.
        val monthFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
        return itemList.groupBy { monthFormat.format(it.date) }
    }

}
