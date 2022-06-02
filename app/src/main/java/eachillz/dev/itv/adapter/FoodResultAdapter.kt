package eachillz.dev.itv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R
import eachillz.dev.itv.api.Hint
import eachillz.dev.itv.databinding.ItemFoodResultBinding
import eachillz.dev.itv.firestore.DailyMealPost
import eachillz.dev.itv.user.User
import java.util.*

class FoodResultAdapter(
    private var foodResult: MutableList<Hint>,
    var insertDB: (DailyMealPost) -> Unit
) :RecyclerView.Adapter<FoodResultAdapter.FoodResultViewHolder>(){

   private var _binding : ItemFoodResultBinding? = null
   private val binding get() = _binding!!



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodResultViewHolder {
        _binding = ItemFoodResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodResultViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: FoodResultViewHolder, position: Int) {
        val foodItem = foodResult[position]
        holder.bind(foodItem, binding, insertDB)
    }

    override fun getItemCount(): Int {
        return foodResult.size
    }

    class FoodResultViewHolder(itemView: ItemFoodResultBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(foodItem: Hint, binding: ItemFoodResultBinding, insertDB: (DailyMealPost) -> Unit) {
            binding.tvFoodResultItemName.text = foodItem.food.label

            var name = foodItem.food.label
            var protein = foodItem.food.nutrients.PROCNT.toLong()
            var calories = foodItem.food.nutrients.ENERC_KCAL.toLong()
            var fat = foodItem.food.nutrients.FAT.toLong()
            var carbs = foodItem.food.nutrients.CHOCDF.toLong()
            var image = foodItem.food.image
            var time = System.currentTimeMillis()
            val dateInString = ""
            var serving = 1

            if(image.isNullOrEmpty()){
                image = "https://firebasestorage.googleapis.com/v0/b/textdemo-9e9b1.appspot.com/o/posts%2FFri%20Sep%2010%2015%3A52%3A09%20PDT%202021.png?alt=media&token=a774304d-da5b-4cb9-8fbc-853f8ff6a78f"
            }


            Picasso.get()
                .load(image)
                .placeholder(R.drawable.bacground_myfood)
                .into(binding.ivFoodResultImage)
            val user: User = User("","")
            var meal = DailyMealPost("",name, protein, calories, fat, carbs, image, serving, time, Date(), user )


            binding.clFoodResult.setOnClickListener {
                insertDB(meal)
            }

        }

    }
}