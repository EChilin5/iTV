package eachillz.dev.itv.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.databinding.UserFoodItemBinding
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R
import eachillz.dev.itv.activity.FoodItemDetailActivity
import eachillz.dev.itv.firestore.DailyMealPost

class UserMealAdapter(private val userList:ArrayList<DailyMealPost>): RecyclerView.Adapter<UserMealAdapter.UserMealAdapterHolder>() {

    private var _binding: UserFoodItemBinding? = null
    private val binding get() = _binding!!

    class UserMealAdapterHolder(itemView: UserFoodItemBinding) : RecyclerView.ViewHolder(itemView.root){
        fun bind(currentItem: DailyMealPost, binding: UserFoodItemBinding) {
            binding.tvFoodName.text = currentItem.name
            binding.tvCalorieCount.text = currentItem.calories.toString()

            if(currentItem.image.isNullOrEmpty()){
                Picasso.get()
                    .load(R.drawable.bacground_myfood)
                    .placeholder(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivUserFoodImage)
            }else{
                Picasso.get()
                    .load(currentItem.image)
                    .placeholder(R.drawable.bacground_myfood)
                    .error(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivUserFoodImage)
            }

            binding.rvUserFurtherDetail.setOnClickListener {
                val intent  = Intent(itemView.context, FoodItemDetailActivity::class.java)
                intent.putExtra("mealItem", currentItem)
                itemView.context.startActivity(intent)
            }





            var response: String
            if(currentItem.name == "Temp User"){
                response = "A preview of nutrition facts or description once a food item is added to the list"
            }else{
                response = "No current description has been found"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMealAdapterHolder {
        _binding = UserFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserMealAdapterHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: UserMealAdapterHolder, position: Int) {
        val currentItem = userList[position]
        holder.bind(currentItem, binding)



//        binding.tvFoodItemDescription.text = response
//            binding.itemFood.setOnClickListener { v -> v.context
//            val intent = Intent(v.context, FoodItemDetailActivity::class.java)
//            intent.putExtra("foodName", currentItem.name)
//            v.context.startActivity(intent)
//
//        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}