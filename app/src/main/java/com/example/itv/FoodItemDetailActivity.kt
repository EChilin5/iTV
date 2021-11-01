package com.example.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.itv.databinding.ActivityFoodItemDetailBinding
import com.example.itv.databinding.ActivityFoodItemDetailBinding.bind
import com.example.itv.databinding.ActivityFoodItemDetailBinding.inflate
import com.example.itv.databinding.ActivityMainBinding

class FoodItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = inflate(layoutInflater)
        setContentView(binding.root)

        val foodName:String = intent?.getStringExtra("foodName").toString()
        binding.tvFoodItemTitle.text = foodName

        binding.ivBackArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



}