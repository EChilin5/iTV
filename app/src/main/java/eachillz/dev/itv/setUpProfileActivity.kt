package eachillz.dev.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import eachillz.dev.itv.databinding.ActivitySetUpProfileBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG ="setUpProfileActivity"
class setUpProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySetUpProfileBinding
    private lateinit var etName : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnSubmit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        etEmail = binding.etEmail
        etPassword = binding.etProfilePassword
        etConfirmPassword = binding.etConfirmPassword
        btnSubmit = binding.btnProfileSubmit

        btnSubmit.setOnClickListener {

            if(etEmail.text.isEmpty() || etConfirmPassword.text.isEmpty() || etConfirmPassword.text.isEmpty()){
                Toast.makeText(this, "Missing Infromation", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            btnSubmit.isEnabled = false

            if(etConfirmPassword.text.toString() == etPassword.text.toString() && etEmail.text.contains("@")){
                createUserAccount(etEmail.text.toString(), etPassword.text.toString())
            }else{
                btnSubmit.isEnabled = true
                Toast.makeText(this, "Invalid email or password do not match", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createUserAccount(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                btnSubmit.isEnabled = true
                if(task.isSuccessful){
                    Log.i(TAG, "created user Successfully")
                    goToMain()

                }else{
                    Log.e(TAG,"failed to create user", task.exception)
                    Toast.makeText(this,"Unable to create account", Toast.LENGTH_SHORT).show()
                }
            }

    }

}