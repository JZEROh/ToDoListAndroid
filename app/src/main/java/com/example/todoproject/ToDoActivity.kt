package com.example.todoproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.adapter.ToDoAdapter
import com.example.todoproject.data.JwtToken
import com.example.todoproject.data.Schedule
import com.example.todoproject.data.Todo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.jsonwebtoken.Jwts
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ToDoActivity : AppCompatActivity() {

    // UI 요소
    private lateinit var btnSave: Button
    private lateinit var backMain: Button
    private lateinit var etTodo: EditText
    private lateinit var toDoList: RecyclerView
    private lateinit var selectedDateTextView: TextView
    private lateinit var btnDelete: Button

    var url : String = "192.168.219.59"
    // 데이터
    private lateinit var toDoItems: MutableList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        // UI 요소 초기화
        btnSave = findViewById(R.id.btnSave)
        backMain = findViewById(R.id.backMain)
        etTodo = findViewById(R.id.etTodo)
        toDoList = findViewById(R.id.toDoList) // RecyclerView ID 수정
        selectedDateTextView = findViewById(R.id.tvSelectedDate)
        btnDelete = findViewById(R.id.btnDelete)

        // toDoItems 초기화
        toDoItems = mutableListOf()

        // RecyclerView 설정
        toDoList.layoutManager = LinearLayoutManager(this)
        val adapter = ToDoAdapter(toDoItems)
        toDoList.adapter = adapter

        // Intent로부터 전달된 날짜를 받아서 텍스트 뷰에 설정
        val selectedDate = intent.getStringExtra("selectedDate")
        if (selectedDate != null) {
            selectedDateTextView.text = selectedDate
        }

        // 저장 버튼 클릭 이벤트 처리
        btnSave.setOnClickListener {
            val title = etTodo.text.toString() // 제목 가져오기
            val content = "" // 내용은 빈 문자열로 설정 (필요에 따라 수정)

            val selectedDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

            val formattedDate = selectedDate?.let {
                val parsedDate = selectedDateFormat.parse(it)
                serverDateFormat.format(parsedDate)
            }

            val todo = Todo(title, formattedDate.toString(), content)

            if (title.isNotEmpty()) { // 빈 제목 체크
                toDoItems.add(todo)
                adapter.notifyDataSetChanged()
                etTodo.text.clear()

                sendScheduleToServer(title, content, formattedDate.toString()) // 수정된 부분
            } else {
                Toast.makeText(this, "할 일을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로가기 버튼 클릭 이벤트 처리
        backMain.setOnClickListener {
            finish() // 액티비티 종료 (메인 화면으로 돌아가기)
        }

        // 서버에서 할 일 목록 가져오기
        getTodoList()
    }

    // 서버에서 할 일 목록 가져오는 함수
    private fun getTodoList() {
        // Http 요청을 보내기위한 OkHttpClient 객체 생성
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                // SharedPreferences에서 토큰 가져오기
                val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("access_token", null)

                // 토큰이 존재하는 경우, 요청 헤더에 추가
                val newRequest = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    originalRequest // 토큰이 없는 경우 원본 요청 그대로 사용
                }

                chain.proceed(newRequest)
            }
            .build()

        // Request.Builder를 사용하여 요청 객체생성
        // GET 메서드를 사용하여 서버에 요청, 헤더에 토큰 추가
        val request = Request.Builder()
            .url("http://${url}:8089/api/schedules")
            .get()
            .build()

        // 비동기 요청 보내기
        // 비동기적으로 요청을 보내고, 콜백을 통해 응답을 처리
        client.newCall(request).enqueue(object : Callback {
            // 요청 실패 시 콜백
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ToDoActivity, "요청 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ToDoActivity", "Failed to get todo list", e) // 에러 로그 출력
                }
            }

            // 요청 성공 시 콜백
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val responseBody = it.body?.string()

                        // Gson을 사용하여 JSON 배열 파싱
                        val listType = object : TypeToken<List<Schedule>>() {}.type
                        val scheduleList: List<Schedule> = Gson().fromJson(responseBody, listType)

                        // 선택된 날짜와 일치하는 스케줄만 필터링
                        val selectedDate = intent.getStringExtra("selectedDate")
                        val filteredScheduleList = scheduleList.filter { schedule ->
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val scheduleDate = dateFormat.parse(schedule.date)
                            val selectedDateObj = selectedDate?.let { dateFormat.parse(it) }

                            scheduleDate?.let {
                                val cal1 = Calendar.getInstance().apply { time = it }
                                val cal2 = Calendar.getInstance().apply { time = selectedDateObj!! }
                                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                                        cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                                        cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
                            } ?: false
                        }

                        runOnUiThread {
                            // toDoItems 업데이트 및 어댑터 갱신
                            toDoItems.clear()
                            toDoItems.addAll(filteredScheduleList.map { Todo(it.title, it.date, it.content) })
                            toDoList.adapter?.notifyDataSetChanged()
                        }
                    } else {
                        // 요청 실패 처리
                        runOnUiThread {
                            Toast.makeText(this@ToDoActivity, "일정 목록 가져오기 실패", Toast.LENGTH_SHORT).show()
                            Log.e("ToDoActivity", "Failed to get todo list: ${response.code}")
                        }
                    }
                }
            }

        })
    }

    private fun sendScheduleToServer(title: String, content: String, date: String) {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                // SharedPreferences에서 토큰 가져오기
                val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("access_token", null)

                // 토큰이 존재하는 경우, 요청 헤더에 추가
                val newRequest = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    originalRequest // 토큰이 없는 경우 원본 요청 그대로 사용
                }

                chain.proceed(newRequest)
            }
            .build()

        // 1. JSON 객체 생성
        val json = JSONObject().apply {
            put("title", title)
            put("content", content)
            put("date", date)
            // 필요에 따라 다른 필드 추가 (예: memberIdx)
        }

        // 2. 요청 Body 생성
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        // 3. Request 객체 생성 (POST 방식)
        val request = Request.Builder()
            .url("http://${url}:8089/api/schedules") // 실제 서버 API 엔드포인트로 변경
            .post(requestBody)
            .build()

        // 4. 비동기 요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 5. 요청 실패 처리
                runOnUiThread {
                    Toast.makeText(this@ToDoActivity, "요청 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("AddScheduleActivity", "Failed to create schedule", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // 6. 요청 성공 처리
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ToDoActivity, "일정 추가 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.body?.string() ?: "알 수 없는 오류 발생"
                        Toast.makeText(this@ToDoActivity, "요청 실패: $errorBody", Toast.LENGTH_SHORT).show()
                        Log.e("AddScheduleActivity", "Failed to create schedule: ${response.code} - $errorBody")
                    }
                }
            }
        })
    }

}