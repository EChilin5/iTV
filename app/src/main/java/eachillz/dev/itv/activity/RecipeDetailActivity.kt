package eachillz.dev.itv.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import eachillz.dev.itv.api.Hit
import eachillz.dev.itv.databinding.ActivityRecipeDetailBinding
import kotlin.math.roundToInt

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding

    private lateinit var recipeInfo : Hit

    private lateinit var ivRecipe:ImageView
    private lateinit var tvTimeAmt:TextView
    private lateinit var tvCalorieAmt:TextView
    private lateinit var tvIngredient: TextView
    private lateinit var btnBack : ImageButton
    private lateinit var tvRecipeName: TextView

    private lateinit var tvRecipeProtien: TextView
    private lateinit var tvRecipeCarbs: TextView
    private lateinit var tvRecipeFat :TextView
    private lateinit var tvServing: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
         recipeInfo = intent.getSerializableExtra("recipeInfo") as Hit

        assignValue()


    }

    private fun assignValue() {
        tvRecipeName.text = recipeInfo.recipe.label
        tvTimeAmt.text = recipeInfo.recipe.totalTime.roundToInt().toString()
        tvCalorieAmt.text = recipeInfo.recipe.calories.roundToInt().toString()

        tvRecipeProtien.text = recipeInfo.recipe.totalDaily.PROCNT.quantity.roundToInt().toString()
        tvRecipeCarbs.text = recipeInfo.recipe.totalDaily.CA.quantity.roundToInt().toString()
        tvRecipeFat.text = recipeInfo.recipe.totalDaily.FAT.quantity.roundToInt().toString()


        var IngredientText = ""
        for(item in recipeInfo.recipe.ingredientLines){
            IngredientText += item +"\n"
        }
        tvIngredient.text = IngredientText
        var image_url = recipeInfo.recipe.image
        Glide.with(this)
            .load(image_url)
            .into(ivRecipe)

        btnBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun initView() {
        tvRecipeName = binding.tvRecipeDetailName
        ivRecipe = binding.ivRecipeDetail
        tvTimeAmt = binding.tvTimeAmt
        tvCalorieAmt = binding.tvRcalorieDetailAmt
        tvIngredient = binding.tvIngrdienList
        btnBack = binding.ivRecipeBack
        tvRecipeProtien = binding.tvRdProteinAmt
        tvRecipeCarbs = binding.tvRDCarbsAmt
        tvRecipeFat = binding.tvRDCarbsAmt
        tvServing = binding.tvRDServingAmt


    }
}