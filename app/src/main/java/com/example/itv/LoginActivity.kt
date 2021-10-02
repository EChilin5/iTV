package com.example.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.itv.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

private const val TAG= "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var btnLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()






    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun initView() {
        btnLogin = binding.btnLogin

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser !=null){
            goToMain()
        }


        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false

            val email = binding.etUserName.text.toString()
            val password = binding.etPasword.text.toString()


            if(binding.etUserName.text.isBlank() || binding.etPasword.text.isBlank()) {
                Toast.makeText(this, "Missing content", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                btnLogin.isEnabled = true
                if(task.isSuccessful){
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                    goToMain()
                }else{
                    Log.i(TAG, "Login Failed", task.exception)
                    Toast.makeText(this, "Authentication Faile", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}