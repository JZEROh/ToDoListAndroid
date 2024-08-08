package com.example.todoproject

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainContent = findViewById<FrameLayout>(R.id.mainContent)
        val bnv = findViewById<BottomNavigationView>(R.id.bnv)

        //프레그먼트 디폴트 화면
        supportFragmentManager.beginTransaction().replace(
            R.id.mainContent, Calendar_frag()
        ).commit()

        bnv.setOnItemSelectedListener { //메뉴 선택
                item ->
            when (item.itemId) {
                R.id.weather_id -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.mainContent, WeatherFragment()
                    ).commit()
                    true
                }

                R.id.calender_id -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.mainContent, Calendar_frag()
                    ).commit()
                    true

                }

                R.id.todolist_id -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.mainContent, RecyclerViewMain()
                    ).commit()
                    true

                }

                else -> false
            }
        }
    }
}