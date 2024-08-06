package com.example.todoproject

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val API_KEY = "04fcb86fd0e51ab3c803ed70290ff646"  // 발급받은 OpenWeatherMap API 키
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        textView = findViewById(R.id.textView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            textView.text = "Location permission required to fetch weather data."
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    // 사용자의 위치에서 도시 이름을 얻어오기
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
                    val city = addresses?.get(0)?.locality ?: "Unknown City"
                    Log.d("locationTag", city)

                    // 날씨 API 요청 URL 생성
                    val weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$API_KEY"

                    val queue: RequestQueue = Volley.newRequestQueue(this)
                    val stringRequest = StringRequest(Request.Method.GET, weatherUrl,
                        { response ->
                            try {
                                val gson = Gson()
                                val jsonObject = gson.fromJson(response, JsonObject::class.java)
                                val main = jsonObject.getAsJsonObject("main")
                                val temp = main.get("temp").asDouble
                                val weather = jsonObject.getAsJsonArray("weather").get(0).asJsonObject
                                val description = weather.get("description").asString
                                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                                textView.text = """
                                    Location: $city
                                    Date: $date
                                    Current Temperature: $temp°C
                                    Weather: $description
                                """.trimIndent()
                            } catch (e: Exception) {
                                textView.text = "Failed to parse weather data!"
                                Log.e("WeatherActivity", "Parsing error: ${e.message}")
                            }
                        },
                        { error ->
                            textView.text = "Failed to get weather data!"
                            Log.e("WeatherActivity", "Network error: ${error.message}")
                        }
                    )
                    queue.add(stringRequest)
                } else {
                    textView.text = "Location not available!"
                }
            }
            .addOnFailureListener { exception ->
                textView.text = "Failed to get location!"
                Log.e("WeatherActivity", "Location error: ${exception.message}")
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchWeatherData()
            } else {
                textView.text = "Permission denied!"
            }
        }
    }
}
