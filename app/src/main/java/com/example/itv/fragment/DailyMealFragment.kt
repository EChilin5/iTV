package com.example.itv.fragment

import android.content.Context
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
import com.example.itv.overlayfood
import com.example.itv.user.UserData
import com.example.itv.user.UserItemDataEntry
import com.example.itv.user.UserMealAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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
    private var calories = 10000
    private var total = 0
    private var totalCalorieProgress = 0
    private var remainingCalorie = 10000
    private var dateFormated = ""

    private val database = Firebase.database
    private lateinit var userRecylerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserItemDataEntry>


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
        dateFormated = date.toString("MM/dd/yyyy")
        binding.tvCurrentDate.text = dateFormated

        binding.fabSave.setOnClickListener{
            Toast.makeText(context, "selected button", Toast.LENGTH_LONG).show()
            openFoodItem(context)
        }

        userRecylerView = binding.rvDailyFood
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<UserItemDataEntry>()

        getUserFoodData()


    }



    private fun getUserFoodData(){
        userArrayList.clear()
        calories = 10000
        total = 0
        totalCalorieProgress = 0
        remainingCalorie = 10000
        val ref = database.getReference("UserMeal")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userArrayList.clear()

                    for(userSnapShot in snapshot.children){
                            val user = userSnapShot.getValue(UserItemDataEntry::class.java)
                        if(user?.date!!.contains(dateFormated)) {
                            calories -= Integer.parseInt( user.calories)
                            total +=  Integer.parseInt( user.calories)
                                userArrayList.add(user)
                            }

                    }
                    if(userArrayList.isEmpty()){
                        val temp = UserItemDataEntry( "Example User" ,  "Temp User","0", "0","0",
                            dateFormated)
                     userArrayList.add(temp)
                    }

                    userRecylerView.adapter = UserMealAdapter(userArrayList)
                }

                binding.tvCurrentCalorieCounter.text = "$total"
                binding.tvRemainingCaloriesCounter.text = "$calories"

                val progress1 = ((total).toDouble() / 10000) *100
                var progress2 = ((remainingCalorie - calories).toDouble() /remainingCalorie) *100
                progress2 = 100 - progress2

                binding.progressBar.progress = progress1.toInt()
                binding.progressBar2.progress = progress2.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "failed to load data", Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun openFoodItem(context: Context?) {

        var dialog = overlayfood()
        dialog.show(childFragmentManager, "overlay")
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



