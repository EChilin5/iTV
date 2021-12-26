package com.example.itv.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.FoodItemDetailActivity
import com.example.itv.databinding.UserFoodItemBinding
import com.example.itv.databinding.UserItemBinding
import com.example.itv.user.UserAdapter.*
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.with
import com.squareup.picasso.Picasso

class UserAdapter(private val userList: ArrayList<UserData>): RecyclerView.Adapter<UserAdapterHolder>() {

    private var _binding : UserFoodItemBinding? = null
    private val binding get() = _binding!!

    class UserAdapterHolder(itemView: UserFoodItemBinding?) : RecyclerView.ViewHolder(itemView?.root!!) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterHolder {
        _binding = UserFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserAdapterHolder(_binding)
    }

    override fun onBindViewHolder(holder: UserAdapterHolder, position: Int) {
        val currentItem = userList[position]
        binding.tvFoodName.text = currentItem.name
        binding.tvCalorieCount.text = currentItem.calories.toString()
        binding.tvFoodItemDescription.text = currentItem.description

        Picasso.get()
            .load("https://firebasestorage.googleapis.com/v0/b/textdemo-9e9b1.appspot.com/o/posts%2Fapple-pink-lady.png?alt=media&token=7811fccb-6c9e-4fdc-a7dd-4ad86e5b7aa6")
            .resize(150, 150)         //optional
            .centerCrop()                        //optional
            .into(binding.ivUserFoodImage)

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