package eachillz.dev.itv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R
import eachillz.dev.itv.databinding.ItemFoodResultBinding
import eachillz.dev.itv.firestore.DailyMealPost

class FoodResultAdapter(
    private var foodResult: MutableList<DailyMealPost>,
    var insertDB: (DailyMealPost) -> Unit,
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class FoodResultViewHolder(itemView: ItemFoodResultBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(
            foodItem: DailyMealPost,
            binding: ItemFoodResultBinding,
            insertDB: (DailyMealPost) -> Unit,
        ) {
            binding.tvFoodResultItemName.text = foodItem.name




            var image = foodItem.image

            if(image.isEmpty()){
                image = "https://firebasestorage.googleapis.com/v0/b/textdemo-9e9b1.appspot.com/o/posts%2FFri%20Sep%2010%2015%3A52%3A09%20PDT%202021.png?alt=media&token=a774304d-da5b-4cb9-8fbc-853f8ff6a78f"
            }


            Picasso.get()
                .load(image)
                .placeholder(R.drawable.bacground_myfood)
                .into(binding.ivFoodResultImage)


            binding.clFoodResult.setOnClickListener {
                insertDB(foodItem)
            }

        }

    }
}