package com.example.todoproject // 실제 패키지 이름으로 변경

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.todoproject.adapter.ViewPagerAdapter
import com.example.todoproject.data.ToDoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewMain : Fragment() {

    private lateinit var viewPager: ViewPager
    private val toDoItems = mutableListOf<ToDoItem>()
    val url : String = "192.168.45.146"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 레이아웃을 인플레이트합니다.
        return inflater.inflate(R.layout.activity_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        getTodoList()
    }

    override fun onResume() {
        super.onResume()
        getTodoList()
    }

    private fun generateMonthlyData(): Map<String, List<ToDoItem>> {
        val monthFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
        return toDoItems.groupBy { monthFormat.format(it.date) }
    }

    private fun getTodoList() {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("access_token", null)
                val newRequest = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    originalRequest
                }
                chain.proceed(newRequest)
            }
            .build()

        val request = Request.Builder()
            .url("http://${url}:8089/api/schedules") // 실제 API 엔드포인트로 변경
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "요청 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("RecyclerViewMain", "Failed to get todo list", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val responseBody = it.body?.string()

                        val listType = object : TypeToken<List<ToDoItem>>() {}.type
                        val scheduleList: List<ToDoItem> = Gson().fromJson(responseBody, listType)

                        requireActivity().runOnUiThread {
                            toDoItems.clear()
                            toDoItems.addAll(scheduleList)

                            if (viewPager.adapter == null) {
                                val adapter = ViewPagerAdapter(childFragmentManager, generateMonthlyData())
                                viewPager.adapter = adapter
                            } else {
                                (viewPager.adapter as ViewPagerAdapter).notifyDataSetChanged()
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "일정 목록 가져오기 실패", Toast.LENGTH_SHORT).show()
                            Log.e("RecyclerViewMain", "Failed to get todo list: ${response.code}")
                        }
                    }
                }
            }
        })
    }
}