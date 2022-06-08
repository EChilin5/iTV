package eachillz.dev.itv.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.databinding.UserFoodItemBinding
import eachillz.dev.itv.user.UserAdapter.*
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R
import eachillz.dev.itv.api.FoodSearchResult

private const val TAG = "UserAdapter"
class UserAdapter(private val userList: MutableList<FoodSearchResult>): RecyclerView.Adapter<UserAdapterHolder>() {

    private var _binding : UserFoodItemBinding? = null
    private val binding get() = _binding!!

    class UserAdapterHolder(itemView: UserFoodItemBinding?) : RecyclerView.ViewHolder(itemView?.root!!) {
        fun bind(currentItem: FoodSearchResult, binding: UserFoodItemBinding) {
            binding.tvFoodName.text = currentItem.text

            Log.i(TAG, "${currentItem.parsed}")

            val parsed = currentItem.parsed
            val calorie = parsed[0].food.nutrients.ENERC_KCAL
            val imageLink = parsed[0].food.image
            binding.tvCalorieCount.text = calorie.toString()

            if(imageLink.isEmpty()){
                Picasso.get()
                    .load(R.drawable.bacground_myfood)
                    .placeholder(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivUserFoodImage)
            }else{
                Picasso.get()
                    .load(imageLink)
                    .placeholder(R.drawable.bacground_myfood)
                    .error(R.drawable.bacground_myfood)
                    .resize(150, 150)         //optional
                    .centerCrop()                        //optional
                    .into(binding.ivUserFoodImage)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterHolder {
        _binding = UserFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserAdapterHolder(_binding)
    }

    override fun onBindViewHolder(holder: UserAdapterHolder, position: Int) {
        val currentItem = userList[position]

        holder.bind(currentItem, binding)





    }

    override fun getItemCount(): Int {
        return userList.size
    }
}