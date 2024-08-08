package com.example.todoproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.todoproject.data.Schedule
import com.example.todoproject.fragment.RecyclerViewFragment
import com.example.todoproject.data.ToDoItem

class ViewPagerAdapter(fm: FragmentManager, private val monthlyData: Map<String, List<Schedule>>) : FragmentPagerAdapter(fm) {

    private val months = monthlyData.keys.toList()

    override fun getCount(): Int {
        return months.size
    }

    override fun getItem(position: Int): Fragment {
        val month = months[position]
        return RecyclerViewFragment.newInstance(month, monthlyData[month] ?: emptyList())
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return months[position]
    }
}