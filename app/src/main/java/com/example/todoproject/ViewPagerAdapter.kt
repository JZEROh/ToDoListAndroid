package com.example.todoproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, private val monthlyData: Map<String, List<ToDoItem>>) : FragmentPagerAdapter(fm) {

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