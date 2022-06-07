package eachillz.dev.itv.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R

import eachillz.dev.itv.databinding.ActivityFoodItemDetailBinding
import eachillz.dev.itv.databinding.ActivityFoodItemDetailBinding.inflate
import eachillz.dev.itv.firestore.DailyMealPost

class FoodItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodItemDetailBinding

    private lateinit var mealInfo : DailyMealPost


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = inflate(layoutInflater)
        setContentView(binding.root)

        mealInfo  = intent.getSerializableExtra("mealItem") as DailyMealPost

        binding.tvDetailsName.text = mealInfo.name
        binding.tvCalorieAmnt.text = mealInfo.calories.toString()
        binding.tvCarbsAmnt.text = mealInfo.carbs.toString()
        binding.tvProteinAmnt.text = mealInfo.protein.toString()
        binding.tvFatAmnt.text = mealInfo.fat.toString()
        binding.tvServingAmnt.text = mealInfo.serving.toString()


        Picasso.get()
            .load(mealInfo.image)
            .placeholder(R.drawable.bacground_myfood)
            .error(R.drawable.bacground_myfood)
            .into(binding.ivFoodImage)

        binding.ivBtnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



}