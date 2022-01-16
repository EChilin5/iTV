package eachillz.dev.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import eachillz.dev.itv.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

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

    private fun openFoodItem() {

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

        binding.tvHelp.setOnClickListener {
            openFoodItem()

        }


        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false

            val email = binding.etUserName.text.toString()
            val password = binding.etPasword.text.toString()


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

            val intent = Intent(this, setUpProfileActivity::class.java)
        startActivity(intent)
        finish()
        //btnSignUp.isEnabled = false

           // createUserAccount(etUserName.text.toString(), etPassword.text.toString())


        }


    }

    private fun createUserAccount(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                btnSignUp.isEnabled = true
                if(task.isSuccessful){
                    Log.i(TAG, "created user Successfully")
                    goToMain()

                }else{
                    Log.e(TAG,"failed to create user", task.exception)
                    Toast.makeText(this,"Unable to login", Toast.LENGTH_SHORT).show()
                }
            }

    }
}