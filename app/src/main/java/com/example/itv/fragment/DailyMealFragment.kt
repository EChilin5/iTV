package com.example.itv.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.databinding.FragmentDailyMealBinding
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

    private var _binding: FragmentDailyMealBinding? = null
    private val binding get() = _binding!!
    private var calories = 3000
    private var total = 0
    private var totalCalorieProgress = 0
    private val remainingCalorie = 9000

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
        _binding = FragmentDailyMealBinding.inflate(inflater, container, false)
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
                binding.tvCurrentCalorieCounter.text = "$total"
                binding.tvRemainingCaloriesCounter.text = "$calories"

                var progress1 = (total / 10000) * 100
                var progress2 = ((remainingCalorie - calories) /remainingCalorie) *100

                Toast.makeText(context, " "+total+" "+progress1 + "  " + progress2, Toast.LENGTH_LONG).show()
                binding.progressBar.progress = progress1
                binding.progressBar2.progress = progress2
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

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