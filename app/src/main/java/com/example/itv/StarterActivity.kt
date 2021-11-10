package com.example.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.itv.databinding.ActivityStarterBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class StarterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStarterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initView()
    }

    private fun initView() {
        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser !=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{

            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, (1500))
        }


    }
}