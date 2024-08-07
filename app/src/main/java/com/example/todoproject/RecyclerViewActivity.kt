package com.example.todoproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val recyclerView: RecyclerView = findViewById(R.id.rvToDoList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            ToDoItem("Title 1", "Detail 1", "2024-08-07"),
            ToDoItem("Title 2", "Detail 2", "2024-08-08"),
            ToDoItem("Title 3", "Detail 3", "2024-08-09")
        )
        val adapter = ToDoAdapter(itemList)
        recyclerView.adapter = adapter
    }
}
