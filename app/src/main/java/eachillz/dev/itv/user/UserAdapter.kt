package eachillz.dev.itv.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.databinding.UserFoodItemBinding
import eachillz.dev.itv.user.UserAdapter.*
import com.squareup.picasso.Picasso
import eachillz.dev.itv.R

class UserAdapter(private val userList: ArrayList<UserData>): RecyclerView.Adapter<UserAdapterHolder>() {

    private var _binding : UserFoodItemBinding? = null
    private val binding get() = _binding!!

    class UserAdapterHolder(itemView: UserFoodItemBinding?) : RecyclerView.ViewHolder(itemView?.root!!) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterHolder {
        _binding = UserFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserAdapterHolder(_binding)
    }

    override fun onBindViewHolder(holder: UserAdapterHolder, position: Int) {
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


    }

    override fun getItemCount(): Int {
        return userList.size
    }
}