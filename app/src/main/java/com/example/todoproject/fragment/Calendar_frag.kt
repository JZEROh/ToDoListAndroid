package com.example.todoproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.todoproject.R
import com.example.todoproject.ToDoActivity
import com.example.todoproject.adapter.Month_adapter
import com.example.todoproject.databinding.CalendarBinding
import java.util.Calendar
import java.util.Date

class Calendar_frag : Fragment(R.layout.calendar) {

    private var _binding: CalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 초기화
        _binding = CalendarBinding.inflate(inflater, container, false)
        val view = binding.root

        // 뷰 초기화 및 데이터 생성
        initView()
        createData()

        return view
    }

    private fun initView() {
        // RecyclerView 초기화 및 어댑터 설정
        val position = Int.MAX_VALUE / 2
        binding.calRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.calRecycler.adapter = Month_adapter { selectedDate ->
            // 날짜가 선택되면 ToDoActivity로 인텐트 생성 및 전달
            val intent = Intent(requireContext(), ToDoActivity::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }

        // item의 초기 위치 지정
        binding.calRecycler.scrollToPosition(position)

        // 항목씩 스크롤 되도록 설정
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calRecycler)
    }

    private fun createData() {
        // 실제 날짜 데이터를 생성하여 어댑터에 설정
        val dateList = generateDateList()
        (binding.calRecycler.adapter as Month_adapter).setData(dateList)
    }

    private fun generateDateList(): List<Date> {
        // 예시 날짜 데이터를 생성하여 반환하는 메서드
        val calendar = Calendar.getInstance()
        val dateList = mutableListOf<Date>()
        for (i in 0 until 12) { // 12달의 날짜 데이터 생성 예시
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.add(Calendar.MONTH, i)
            for (j in 0 until calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                dateList.add(calendar.time)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return dateList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 바인딩 해제
        _binding = null
    }
}
