package com.example.itv.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.FoodItemDetailActivity
import com.example.itv.databinding.UserFoodItemBinding
import com.squareup.picasso.Picasso

class UserMealAdapter(private val userList:ArrayList<UserItemDataEntry>): RecyclerView.Adapter<UserMealAdapter.UserMealAdapterHolder>() {

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

        Picasso.get()
            .load(currentItem.img)
            .resize(150, 150)         //optional
            .centerCrop()                        //optional
            .into(binding.ivUserFoodImage)


        var response: String
        if(currentItem.name == "Temp User"){
            response = "A preview of nutrition facts or description once a food item is added to the list"
        }else{
            response = "No current description has been found"
        }

//        binding.tvFoodItemDescription.text = response
//            binding.itemFood.setOnClickListener { v -> v.context
//            val intent = Intent(v.context, FoodItemDetailActivity::class.java)
//            intent.putExtra("foodName", currentItem.name)
//            v.context.startActivity(intent)
//
//        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}