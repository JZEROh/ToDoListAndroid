package com.example.todoproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoproject.data.JwtToken
import com.example.todoproject.databinding.ActivityFirstpageBinding
import com.example.todoproject.databinding.ActivityLoginBinding
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val id = binding.loginID.text.toString()
            val pw = binding.loginPw.text.toString()
            Login(id, pw)
        }

        binding.btnJoin.setOnClickListener{
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun Login(id: String, pw: String) {
        // Http 요청을 보내기위한 OkHttpClient 객체 생성
        val client = OkHttpClient()

        // JSON 객체 생성
        val json = JSONObject()
        json.put("id", id)
        json.put("pw", pw)

        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = RequestBody.create(JSON_MEDIA_TYPE, json.toString())

        // Request.Builder를 사용하여 요청 객체생성
        // POST메서드를 사용하여 폼 데이터 서버로 전송
        val request = Request.Builder()
            .url("http://192.168.219.59:8089/members/sign-in")
            .post(body)
            .build()

        // 비동기 요청 보내기
        // 비동기적으로 요청을 보내고, 콜백을 통해 응답을 처리
        client.newCall(request).enqueue(object : Callback {
            // 요청이 실패했을 때 호출되며, UI스레드에서 실패메세지를 Toast로 표시
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(this@LoginActivity,"요청실패", Toast.LENGTH_SHORT).show()
                }
            }
            // 메서드는 요청이 성공했을 때 호출되며, 응답이 성공적인지 확인하고 성공 메시지를 Toast로 표시
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val jwtToken = Gson().fromJson(it.body?.string(), JwtToken::class.java) // JSON 응답을 JwtToken 객체로 변환
                        val accessToken = jwtToken.accessToken

                        // 토큰 저장 (예시: SharedPreferences 사용)
                        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("access_token", accessToken).apply()

                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "로그인 성공!!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        throw IOException("Unexpected code $it")
                    }
                }
            }
        })
    }
}


