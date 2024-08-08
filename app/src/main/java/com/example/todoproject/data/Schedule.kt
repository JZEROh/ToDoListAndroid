package com.example.todoproject.data

data class Schedule(
    val idx: Long,
    val title: String,
    val content: String,
    val date: String, // 서버에서 반환하는 날짜 형식에 맞춰야 합니다. (예: "yyyy-MM-dd HH:mm:ss")
    val memberIdx: Long
)
