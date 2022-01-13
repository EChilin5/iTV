package eachillz.dev.itv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import eachillz.dev.itv.databinding.ActivityFoodItemDetailBinding
import eachillz.dev.itv.databinding.ActivityFoodItemDetailBinding.inflate

class FoodItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = inflate(layoutInflater)
        setContentView(binding.root)

        val foodName:String = intent?.getStringExtra("foodName").toString()
        binding.tvFoodItemTitle.text = foodName

        binding.ivBackArrow.setOnClickListener {
            val intent = Intent(this, eachillz.dev.itv.MainActivity::class.java)
            startActivity(intent)
        }
    }



}