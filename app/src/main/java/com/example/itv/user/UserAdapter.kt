package com.example.itv.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.databinding.UserItemBinding
import com.example.itv.user.UserAdapter.*

class UserAdapter(private val userList: ArrayList<UserData>): RecyclerView.Adapter<UserAdapterHolder>() {

    private var _binding : UserItemBinding? = null
    private val binding get() = _binding!!

    class UserAdapterHolder(itemView: UserItemBinding?) : RecyclerView.ViewHolder(itemView?.root!!) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterHolder {
        _binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserAdapterHolder(_binding)
    }

    override fun onBindViewHolder(holder: UserAdapterHolder, position: Int) {
        val currentItem = userList[position]
        binding.tvFirstName.text = currentItem.firstName
        binding.tvUserLastName.text = currentItem.lastName
        binding.tvUserAge.text = currentItem.age.toString()
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}