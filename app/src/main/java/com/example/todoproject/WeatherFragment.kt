package com.example.todoproject

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*

class WeatherFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var weatherImageView: ImageView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val API_KEY = "04fcb86fd0e51ab3c803ed70290ff646"  // 발급받은 OpenWeatherMap API 키
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃을 Inflate 합니다.
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment의 View를 초기화합니다.
        textView = view.findViewById(R.id.textView)
        weatherImageView = view.findViewById(R.id.weatherImageView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            textView.text = "Location permission required to fetch weather data."
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    // 사용자의 위치에서 도시 이름을 얻어오기
                    fetchCityName(lat, lon) { city ->
                        // 날씨 API 요청 URL 생성
                        val weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$API_KEY"

                        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
                        val stringRequest = StringRequest(Request.Method.GET, weatherUrl,
                            { response ->
                                try {
                                    val gson = Gson()
                                    val jsonObject = gson.fromJson(response, JsonObject::class.java)
                                    val main = jsonObject.getAsJsonObject("main")
                                    val temp = main.get("temp").asDouble
                                    val weather = jsonObject.getAsJsonArray("weather").get(0).asJsonObject
                                    val description = weather.get("description").asString
                                    val icon = weather.get("icon").asString  // 날씨 아이콘 ID 가져오기
                                    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                                    textView.text = """
                                        Location: $city
                                        Date: $date
                                        Current Temperature: $temp°C
                                        Weather: $description
                                    """.trimIndent()

                                    // 날씨 아이콘에 따라 이미지뷰를 업데이트합니다.
                                    updateWeatherIcon(icon)
                                } catch (e: Exception) {
                                    textView.text = "Failed to parse weather data!"
                                    Log.e("WeatherFragment", "Parsing error: ${e.message}")
                                }
                            },
                            { error ->
                                textView.text = "Failed to get weather data!"
                                Log.e("WeatherFragment", "Network error: ${error.message}")
                            }
                        )
                        queue.add(stringRequest)
                    }
                } else {
                    textView.text = "Location not available!"
                }
            }
            .addOnFailureListener { exception ->
                textView.text = "Failed to get location!"
                Log.e("WeatherFragment", "Location error: ${exception.message}")
            }
    }

    private fun fetchCityName(lat: Double, lon: Double, callback: (String) -> Unit) {
        val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lon&zoom=10&addressdetails=1"
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val gson = Gson()
                    val jsonObject = gson.fromJson(response, JsonObject::class.java)
                    val address = jsonObject.getAsJsonObject("address")
                    val city = address.get("city")?.asString ?: address.get("town")?.asString ?: address.get("village")?.asString ?: "Unknown City"
                    callback(city)
                } catch (e: Exception) {
                    Log.e("WeatherFragment", "Parsing error: ${e.message}")
                    callback("Unknown City")
                }
            },
            { error ->
                Log.e("WeatherFragment", "Network error: ${error.message}")
                callback("Unknown City")
            }
        )
        queue.add(stringRequest)
    }

    private fun updateWeatherIcon(icon: String) {
        val resourceId = when (icon) {
            "01d", "01n" -> R.drawable.clear_sky
            "02d", "02n" -> R.drawable.few_clouds
            "03d", "03n" -> R.drawable.scattered_clouds
            "04d", "04n" -> R.drawable.broken_clouds
            "09d", "09n" -> R.drawable.shower_rain
            "10d", "10n" -> R.drawable.rain
            "11d", "11n" -> R.drawable.thunderstorm
            "13d", "13n" -> R.drawable.snow
            "50d", "50n" -> R.drawable.mist
            else -> R.drawable.unknown
        }
        weatherImageView.setImageResource(resourceId)
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
