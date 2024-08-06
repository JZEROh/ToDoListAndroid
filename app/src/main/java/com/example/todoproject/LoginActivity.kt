package com.example.todoproject

import android.content.ContextParams
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    lateinit var queue : RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val btnGo = findViewById<Button>(R.id.btnGo)
        val etId = findViewById<EditText>(R.id.etId)
        val etPw = findViewById<EditText>(R.id.etPw)

        queue =Volley.newRequestQueue(this)
        btnGo.setOnClickListener {
            var inputId = etId.text.toString()
            var inputPw = etPw.text.toString()

            val member = Member(inputId,inputPw)

            val request =object : StringRequest(
                Request.Method.POST,
                "http://192.168.219.64:8089/join",
                {response->
                    Log.d("response",response.toString())
                },
                { error->
                    Log.d("error",error.toString())
                }
            ){
                override fun getParams(): Map<String,String>{
                    val params :MutableMap<String,String> = HashMap<String,String>()
                    params.put("joinMember",Gson().toJson(member))
                    return params
                }
            }
            queue.add(request)
        }
    }

}