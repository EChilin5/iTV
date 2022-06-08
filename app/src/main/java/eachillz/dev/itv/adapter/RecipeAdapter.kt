package eachillz.dev.itv.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R
import eachillz.dev.itv.activity.RecipeDetailActivity
import eachillz.dev.itv.api.Hit
import eachillz.dev.itv.databinding.ItemRecipeResultBinding
import kotlin.math.roundToInt

class RecipeAdapter(private var recipe : MutableList<Hit>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var _binding: ItemRecipeResultBinding? =null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        _binding = ItemRecipeResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipeItem = recipe[position]
        holder.bind(recipeItem, binding)
    }

    override fun getItemCount(): Int {
        return recipe.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class RecipeViewHolder(itemView: ItemRecipeResultBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(hit: Hit, binding: ItemRecipeResultBinding) {

            var calories = hit.recipe.calories.roundToInt()
            val serving  = hit.recipe.yield
            calories = calories.div(serving).toInt()

            binding.tvRecipeFoodName.text = hit.recipe.label
            binding.tvRecipeCalorieCount.text = "$calories kcal"
            val time = "${hit.recipe.totalTime.roundToInt()} mins"

            binding.tvRecipeTimeAmt.text = time

            val imageUrl = hit.recipe.image
            if(imageUrl.isEmpty()){
                Picasso.get()
                    .load(R.drawable.bacground_myfood)
                    .placeholder(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivRecipeFoodImage)
            }else{
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.bacground_myfood)
                    .error(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivRecipeFoodImage)
            }


            binding.rvRecipeFurtherDetail.setOnClickListener {
                val intent  = Intent(itemView.context, RecipeDetailActivity::class.java)
                intent.putExtra("recipeInfo", hit)
                itemView.context.startActivity(intent)
            }


        }

    }
}