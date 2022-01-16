package eachillz.dev.itv.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.databinding.UserFoodItemBinding
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R

class UserMealAdapter(private val userList:ArrayList<UserItemDataEntry>): RecyclerView.Adapter<UserMealAdapter.UserMealAdapterHolder>() {

    private var _binding: UserFoodItemBinding? = null
    private val binding get() = _binding!!

    class UserMealAdapterHolder(itemView: UserFoodItemBinding) : RecyclerView.ViewHolder(itemView.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMealAdapterHolder {
        _binding = UserFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserMealAdapterHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: UserMealAdapterHolder, position: Int) {
        val currentItem = userList[position]
        binding.tvFoodName.text = currentItem.name
        binding.tvCalorieCount.text = currentItem.calories.toString()

        if(currentItem.img.isNullOrEmpty()){
            Picasso.get()
                .load(R.drawable.bacground_myfood)
                .placeholder(R.drawable.bacground_myfood)
                .resize(150, 150)         //optional
                .centerCrop()                        //optional
                .into(binding.ivUserFoodImage)
        }else{
            Picasso.get()
                .load(currentItem.img)
                .placeholder(R.drawable.bacground_myfood)
                .error(R.drawable.bacground_myfood)
                .resize(150, 150)         //optional
                .centerCrop()                        //optional
                .into(binding.ivUserFoodImage)
        }





        var response: String
        if(currentItem.name == "Temp User"){
            response = "A preview of nutrition facts or description once a food item is added to the list"
        }else{
            response = "No current description has been found"
        }

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