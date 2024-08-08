package com.example.todoproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.todoproject.fragment.Calendar_frag
import com.example.todoproject.fragment.WeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.jsonwebtoken.Jwts

class MainActivity : AppCompatActivity() {
    lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainContent = findViewById<FrameLayout>(R.id.mainContent)
        val bnv = findViewById<BottomNavigationView>(R.id.bnv)
        val logoutBtn = findViewById<TextView>(R.id.LogoutBtn)
        getNick()
        findViewById<TextView>(R.id.mainTvNick).text = nickname

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

        logoutBtn.setOnClickListener{
            logout()
        }
    }


    fun logout(){
        // SharedPreferences에서 토큰 제거
        val sharedPreferences = getSharedPreferences("my_prefs",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("access_token")
        editor.apply()

        // 로그인 화면으로 이동
        val intent = Intent(this,FirstpageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    fun getNick() {
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)
        if (token != null) {
            try {
                val claims = Jwts.parserBuilder()
                    .setSigningKey("secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey") // 서버와 동일한 비밀 키 사용
                    .build()
                    .parseClaimsJws(token)
                    .body

                nickname = claims.get("nick", String::class.java) // 닉네임 추출
            } catch (e: Exception) {
                // 토큰 파싱 실패 처리 (예: 토큰 만료, 유효하지 않은 토큰 등)
                Log.e("JWT", "토큰 파싱 에러", e)
            }
        } else {
            nickname = "불러올 수 없습니다"
        }
    }
}