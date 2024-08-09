package com.example.todoproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class JoinActivity : AppCompatActivity() {

    lateinit var etId : EditText
    lateinit var etPw : EditText
    lateinit var etNick : EditText
    lateinit var btnJoinAct : Button
    var url : String = "192.168.219.59"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        etId = findViewById(R.id.etId)
        etPw = findViewById(R.id.etPw)
        etNick = findViewById(R.id.etNick)
        btnJoinAct = findViewById(R.id.btnJoinAct)

        btnJoinAct.setOnClickListener {
            val id = etId.text.toString()
            val pw = etPw.text.toString()
            val nick = etNick.text.toString()
            registerUser(id, pw, nick)
        }

    }

    // requestUser를 사용하여 OkhttpClient를 사용하여 서버에 POST요청
    private fun registerUser(id: String, pw: String, nick: String) {
        // Http 요청을 보내기위한 OkHttpClient 객체 생성
        val client = OkHttpClient()

        // JSON 객체 생성
        val json = JSONObject()
        json.put("id", id)
        json.put("pw", pw)
        json.put("nick", nick)

        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = RequestBody.create(JSON_MEDIA_TYPE, json.toString())

        // Request.Builder를 사용하여 요청 객체생성
        // POST메서드를 사용하여 폼 데이터 서버로 전송
        val request = Request.Builder()
            .url("http://${url}:8089/members/sign-up")
            .post(body)
            .build()

        // 비동기 요청 보내기
        // 비동기적으로 요청을 보내고, 콜백을 통해 응답을 처리
        client.newCall(request).enqueue(object : Callback {
            // 요청이 실패했을 때 호출되며, UI스레드에서 실패메세지를 Toast로 표시
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(this@JoinActivity,"요청실패",Toast.LENGTH_SHORT).show()
                }
            }
            // 메서드는 요청이 성공했을 때 호출되며, 응답이 성공적인지 확인하고 성공 메시지를 Toast로 표시
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if(!it.isSuccessful) throw IOException("Unexpected code $it")
                    runOnUiThread {
                        Toast.makeText(this@JoinActivity, "회원가입 성공!!", Toast.LENGTH_SHORT).show()
                        val  intent = Intent(this@JoinActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        })

    }


}