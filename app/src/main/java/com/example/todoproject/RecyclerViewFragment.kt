package com.example.todoproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewFragment : Fragment() {

    private lateinit var month: String
    private lateinit var items: List<ToDoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            month = it.getString(ARG_MONTH) ?: ""
            items = it.getParcelableArrayList(ARG_ITEMS) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_recycler_view, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvToDoList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ToDoMonthAdapter(requireContext(), items)
        return view
    }

    companion object {
        private const val ARG_MONTH = "month"
        private const val ARG_ITEMS = "items"

        @JvmStatic
        fun newInstance(month: String, items: List<ToDoItem>) =
            RecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MONTH, month)
                    putParcelableArrayList(ARG_ITEMS, ArrayList(items))
                }
            }
    }
}
