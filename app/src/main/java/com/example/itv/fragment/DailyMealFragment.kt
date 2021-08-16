package com.example.itv.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.databinding.FragmentSettingBinding
import com.example.itv.user.UserData
import com.example.itv.user.UserMealAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [DailyMealFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyMealFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private var calories = 3000
    private var total = 0

    private val database = Firebase.database
    private lateinit var userRecylerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = getCurrentDateTime()
        val dateInString = date.toString("MM/dd/yyyy")
        binding.tvCurrentDate.text = dateInString

        userRecylerView = binding.rvDailyFood
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<UserData>()

        getUserFoodData()
    }

    private fun saveUserInfo() {
//        try {
//            database = Firebase.database.reference
//            val user = UserData("rex","ter", 24)
//            database.child("Users").child("rex").setValue(user)
//        }catch (e: Exception){
//            Toast.makeText(context, "failed to write to db " + e.localizedMessage, Toast.LENGTH_LONG).show()
//        }

    }

    private fun getUserFoodData(){
        val ref = database.getReference("Food")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(userSnapShot in snapshot.children){
                        val user = userSnapShot.getValue(UserData::class.java)
                        calories -= user?.calories!!
                        total += user.calories!!
                        userArrayList.add(user)
                    }

                    userRecylerView.adapter = UserMealAdapter(userArrayList)
                }
                binding.tvTotalCalories.text = "$total"
                binding.tvRemaingCalorie.text = "$calories"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "failed to load data", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getUserNutritionInfo(){

    }


    private fun getCurrentDateTime():Date {
        return Calendar.getInstance().time
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyMealFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}