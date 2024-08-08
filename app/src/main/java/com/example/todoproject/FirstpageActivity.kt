package com.example.todoproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todoproject.databinding.ActivityFirstpageBinding


class FirstpageActivity : AppCompatActivity() {
lateinit var binding: ActivityFirstpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moveLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.moveJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }



//        if (savedInstanceState == null) {
//            replaceFragment(Calendar_frag())
//        }
    }

//    private fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//    }
}
