package com.example.todoproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    lateinit var queue : RequestQueue
    lateinit var btnGo : Button
    lateinit var etId : EditText
    lateinit var etPw : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnGo = findViewById(R.id.btnGo)
        etId = findViewById(R.id.etId)
        etPw = findViewById(R.id.etPw)

        queue =Volley.newRequestQueue(this)

        btnGo.setOnClickListener {
            Log.d("LoginActivity", "Button clicked")
            val id = etId.text.toString()
            val pw = etPw.text.toString()

            val member = Member(id,pw)

            val request =object : StringRequest(
                Request.Method.POST,
                "http://192.168.219.64:8089/members/sign-in",
                {response->
                    Log.d("response",response.toString())
                },
                { error->
                    Log.d("error",error.toString())
                }
            ){
                override fun getParams(): Map<String,String>{
                    val params :MutableMap<String,String> = HashMap<String,String>()
                    params.put("loginDto",Gson().toJson(member))
                    return params
                }
            }
            queue.add(request)
        }
    }
}