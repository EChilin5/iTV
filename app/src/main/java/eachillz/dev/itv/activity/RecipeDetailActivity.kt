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

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding

    private lateinit var recipeInfo : Hit

    private lateinit var ivRecipe:ImageView
    private lateinit var tvTimeAmt:TextView
    private lateinit var tvCalorieAmt:TextView
    private lateinit var tvIngredient: TextView
    private lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
         recipeInfo = intent.getSerializableExtra("recipeInfo") as Hit

        assignValue()


    }

    private fun assignValue() {
        tvTimeAmt.text = recipeInfo.recipe.totalTime.toString()
        tvCalorieAmt.text = recipeInfo.recipe.calories.toString()
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
        ivRecipe = binding.ivRecipeDetail
        tvTimeAmt = binding.tvTimeAmt
        tvCalorieAmt = binding.tvRcalorieDetailAmt
        tvIngredient = binding.tvIngrdienList
        btnBack = binding.ivRecipeBack


    }
}