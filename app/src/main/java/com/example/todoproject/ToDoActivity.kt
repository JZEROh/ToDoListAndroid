package com.example.todoproject

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.databinding.ActivityToDoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ToDoActivity : AppCompatActivity() {

    lateinit var btnSave : Button
    lateinit var backMain : Button
    lateinit var etTodo : EditText
    lateinit var toDoList : RecyclerView
    lateinit var selectedDateTextView: TextView

    // 할 일 목록을 저장할 리스트
    val toDoItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        btnSave = findViewById(R.id.btnSave)
        backMain = findViewById(R.id.backMain)
        etTodo = findViewById(R.id.etTodo)
        toDoList = findViewById(R.id.toDoList) // RecyclerView를 xml에서 가져옴
        selectedDateTextView = findViewById(R.id.tvSelectedDate)

        toDoList.layoutManager = LinearLayoutManager(this)
        val adapter = ToDoAdapter(toDoItems)
        toDoList.adapter = adapter

        // 선택된 날짜를 출력
        selectedDateTextView.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            val todo = etTodo.text.toString()
            if (todo.isNotEmpty()) {
                toDoItems.add(todo)
                adapter.notifyDataSetChanged()
                etTodo.text.clear()
            }else{
                Toast.makeText(this, "할 일을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        backMain.setOnClickListener {
           finish() // 메인화면으로 돌아가기
        }

    }

    fun showDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDateTextView.text = dateFormat.format(selectedDate.time)
            },year,month,day)
        datePickerDialog.show()
    }

}
