package com.example.todoproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecyclerViewMain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 레이아웃을 인플레이트합니다.
        return inflater.inflate(R.layout.activity_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(childFragmentManager, generateMonthlyData())
        viewPager.adapter = adapter
    }

    private fun generateMonthlyData(): Map<String, List<ToDoItem>> {
        // 예제 데이터를 생성하는 함수입니다.
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val itemList = listOf(
            ToDoItem("제목1", "설명1", dateFormat.parse("20240203") ?: Date()),
            ToDoItem("제목2", "설명2", dateFormat.parse("20240201") ?: Date()),
            ToDoItem("제목3", "설명3", dateFormat.parse("20240306") ?: Date()),
            ToDoItem("제목4", "설명4", dateFormat.parse("20240401") ?: Date())
        )

        // 월별로 데이터를 그룹화합니다.
        val monthFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
        return itemList.groupBy { monthFormat.format(it.date) }
    }
}
