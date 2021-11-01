package com.example.itv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.itv.databinding.FragmentCameraBinding
import com.example.itv.databinding.FragmentOverlayfoodBinding
import com.example.itv.user.UserItemDataEntry
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class overlayfood : DialogFragment() {


    private var _binding: FragmentOverlayfoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOverlayfoodBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //name, calories, carbs, protein


        val date = getCurrentDateTime()
        val dateInString = date.toString("MM/dd/yyyy")


        binding.btnPost.setOnClickListener {
            val name = binding.etName.text.toString()
            val calories = binding.etCalories.text.toString()
            val carbs = binding.etCarbs.text.toString()
            val protein = binding.etProtein.text.toString()
            if(name != "") {
                saveUserItem(name, calories, carbs, protein, dateInString)
                Toast.makeText(context, "Post is Saved", Toast.LENGTH_LONG).show()
            }
            this.dismiss()
        }
    }

    private fun saveUserItem(name: String?, calories: String?, carbs: String?, protein: String?, dateInString: String) {
        try {
            val userName = "ed"
            val dateNow = Calendar.getInstance().time
            database = Firebase.database.reference
            val user = UserItemDataEntry(userName,name, carbs,calories,protein, dateInString)
            database.child("UserMeal").child(userName+dateNow).setValue(user)
        }catch (e: Exception){
            Toast.makeText(context, "failed to write to db " + e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            overlayfood().apply {
                arguments = Bundle().apply {

                }
            }
    }
}