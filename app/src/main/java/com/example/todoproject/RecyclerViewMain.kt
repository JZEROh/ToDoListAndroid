package com.example.todoproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.todoproject.adapter.ViewPagerAdapter
import com.example.todoproject.data.Schedule
import com.example.todoproject.data.ToDoItem

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecyclerViewMain : Fragment() {

    lateinit var itemList: List<Schedule>


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

    private fun generateMonthlyData(): Map<String, List<Schedule>> {
        // 예제 데이터를 생성하는 함수입니다.
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        // 월별로 데이터를 그룹화합니다.
        val monthFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
        return itemList.groupBy { monthFormat.format(it.date) }
    }
}
