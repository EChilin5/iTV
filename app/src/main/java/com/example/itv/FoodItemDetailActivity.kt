package com.example.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.itv.databinding.ActivityFoodItemDetailBinding
import com.example.itv.databinding.ActivityFoodItemDetailBinding.bind
import com.example.itv.databinding.ActivityFoodItemDetailBinding.inflate
import com.example.itv.databinding.ActivityMainBinding
import com.example.itv.databinding.FragmentImageUploadBinding

class FoodItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityFoodItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName:String = intent?.getStringExtra("foodName").toString()
        binding.tvFoodItemTitle.text = foodName

        binding.ivBackArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



}