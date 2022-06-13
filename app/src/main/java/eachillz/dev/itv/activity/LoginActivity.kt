package eachillz.dev.itv.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import eachillz.dev.itv.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import eachillz.dev.itv.overlay.forgotPasswordOverlay

private const val TAG= "LoginActivity"
class LoginActivity : AppCompatActivity() {



    private lateinit var binding: ActivityLoginBinding
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()


    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun resetPassword() {

        val dialog = forgotPasswordOverlay()
        val fm = this.supportFragmentManager
        dialog.show(fm, "name")
    }

    private fun initView() {
        btnLogin = binding.btnLogin
        btnSignUp = binding.btnSignup

        etUserName = binding.etUserName
        etPassword = binding.etPasword

        val auth = FirebaseAuth.getInstance()


        if(auth.currentUser!= null){
            goToMain()
        }

        binding.tvHelp.setOnClickListener {
            resetPassword()

        }


        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false

            val email = binding.etUserName.text.toString().trim()
            val password = binding.etPasword.text.toString().trim()


            if(binding.etUserName.text.isBlank() || binding.etPasword.text.isBlank()) {
                btnLogin.isEnabled = true
                Toast.makeText(this, "Missing information", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                btnLogin.isEnabled = true
                if(task.isSuccessful){
                    goToMain()
                }else{
                    Log.i(TAG, "Login Failed", task.exception)
                    Toast.makeText(this, "Unable to login", Toast.LENGTH_SHORT).show()
                }
            }


        }

        btnSignUp.setOnClickListener {

            val intent = Intent(this, SetUpProfileActivity::class.java)
        startActivity(intent)
        finish()
        //btnSignUp.isEnabled = false

           // createUserAccount(etUserName.text.toString(), etPassword.text.toString())


        }


    }

}