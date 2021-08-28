package com.example.itv.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.FoodItemDetailActivity
import com.example.itv.databinding.UserFoodItemBinding

class UserMealAdapter(private val userList:ArrayList<UserData>): RecyclerView.Adapter<UserMealAdapter.UserMealAdapterHolder>() {

    private var _binding: UserFoodItemBinding? = null
    private val binding get() = _binding!!

    class UserMealAdapterHolder(itemView: UserFoodItemBinding) : RecyclerView.ViewHolder(itemView.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMealAdapterHolder {
        _binding = UserFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserMealAdapterHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: UserMealAdapterHolder, position: Int) {
        val currentItem = userList[position]
        binding.tvFoodName.text = currentItem.name
        binding.tvCalorieCount.text = currentItem.calories.toString()
        binding.tvFoodItemDescription.text = currentItem.description
        binding.itemFood.setOnClickListener { v -> v.context
            val intent = Intent(v.context, FoodItemDetailActivity::class.java)
            intent.putExtra("foodName", currentItem.name)
            v.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}