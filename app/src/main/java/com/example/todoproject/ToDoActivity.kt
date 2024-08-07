package com.example.todoproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.Locale

class ToDoActivity : AppCompatActivity() {

    lateinit var btnSave: Button
    lateinit var backMain: Button
    lateinit var etTodo: EditText
    lateinit var toDoList: RecyclerView
    lateinit var selectedDateTextView: TextView

    // 할 일 목록을 저장할 리스트
    val toDoItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        btnSave = findViewById(R.id.btnSave)
        backMain = findViewById(R.id.backMain)
        etTodo = findViewById(R.id.etTodo)
        toDoList = findViewById(R.id.toDoList)
        selectedDateTextView = findViewById(R.id.tvSelectedDate)

        toDoList.layoutManager = LinearLayoutManager(this)
        val adapter = ToDoAdapter(toDoItems)
        toDoList.adapter = adapter

        // Intent로부터 전달된 날짜를 받아서 텍스트 뷰에 설정
        val selectedDate = intent.getStringExtra("selectedDate")
        if (selectedDate != null) {
            selectedDateTextView.text = selectedDate
        }

        btnSave.setOnClickListener {
            val todo = etTodo.text.toString()
            if (todo.isNotEmpty()) {
                toDoItems.add(todo)
                adapter.notifyDataSetChanged()
                etTodo.text.clear()
            } else {
                Toast.makeText(this, "할 일을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        backMain.setOnClickListener {
            finish() // 메인화면으로 돌아가기
        }
    }
}
