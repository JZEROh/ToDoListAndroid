package com.example.weatherapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.todoproject.R
import com.google.gson.Gson
import com.google.gson.JsonObject

class WeatherActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private val API_KEY = "04fcb86fd0e51ab3c803ed70290ff646"  // 발급받은 OpenWeatherMap API 키
    private val URL = "http://api.openweathermap.org/data/2.5/weather?q=Seoul&units=metric&appid=$API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        textView = findViewById(R.id.textView)

        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        val queue: RequestQueue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET, URL,
            { response ->
                val gson = Gson()
                val jsonObject = gson.fromJson(response, JsonObject::class.java)
                val main = jsonObject.getAsJsonObject("main")
                val temp = main.get("temp").asDouble
                textView.text = "Current Temperature: $temp°C"
            },
            { error ->
                textView.text = "Failed to get data!"
            }
        )

        queue.add(stringRequest)
    }
}
