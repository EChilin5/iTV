package eachillz.dev.itv.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R
import eachillz.dev.itv.activity.RecipeDetailActivity
import eachillz.dev.itv.api.Hit
import eachillz.dev.itv.databinding.ItemRecipeResultBinding
import eachillz.dev.itv.databinding.UserFoodItemBinding
import kotlin.math.roundToInt
import kotlin.math.roundToLong

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
        fun bind(hit: Hit, binding: ItemRecipeResultBinding) {
            binding.tvRecipeFoodName.text = hit.recipe.label
            binding.tvRecipeCalorieCount.text = hit.recipe.calories.roundToInt().toString()
            var time = hit.recipe.totalTime.roundToInt()

            binding.tvRecipeTimeAmt.text = time.toString()

            var image_url = hit.recipe.image
            if(image_url.isNullOrEmpty()){
                Picasso.get()
                    .load(R.drawable.bacground_myfood)
                    .placeholder(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivRecipeFoodImage)
            }else{
                Picasso.get()
                    .load(image_url)
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