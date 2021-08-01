package com.example.itv.fragment

import android.os.Bundle
import android.provider.ContactsContract
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.R
import com.example.itv.databinding.FragmentHomeBinding
import com.example.itv.user.UserAdapter
import com.example.itv.user.UserData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val database = Firebase.database
    private lateinit var userRecylerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecylerView  = binding.rvHome
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<UserData>()
        getUserData()
    }

    private fun getUserData(){
        val ref = database.getReference("Users")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(userSnapShot in snapshot.children){
                        val user = userSnapShot.getValue(UserData::class.java)
                        userArrayList.add(user!!)
                    }

                    userRecylerView.adapter = UserAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "failed to load data", Toast.LENGTH_LONG).show()
            }

        })
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}