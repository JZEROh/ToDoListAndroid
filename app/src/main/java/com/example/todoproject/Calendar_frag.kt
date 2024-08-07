package com.example.todoproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.databinding.CalendarBinding

class Calendar_frag : Fragment(R.layout.calendar) {

    private var _binding: CalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarBinding.inflate(inflater, container, false)
        val view = binding.root

        initView()
        createData()

        return view
    }

    private fun initView() {
        val position = Int.MAX_VALUE / 2

        binding.calRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.calRecycler.adapter = Month_adpater { selectedDate ->
            // 날짜가 선택되면 ToDoActivity로 인텐트 생성 및 전달
            val intent = Intent(requireContext(), ToDoActivity::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }

        // item의 위치 지정
        binding.calRecycler.scrollToPosition(position)

        // 항목씩 스크롤 지정
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calRecycler)
    }

    private fun createData() {
        binding.calRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
