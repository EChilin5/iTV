package com.example.itv.fragment

import android.os.Bundle
import android.provider.ContactsContract
import android.renderscript.Sampler
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itv.R
import com.example.itv.databinding.ActivityFoodItemDetailBinding.inflate
import com.example.itv.databinding.FragmentHomeBinding
import com.example.itv.user.UserAdapter
import com.example.itv.user.UserData
import com.google.firebase.auth.ktx.auth
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
        setHasOptionsMenu(true)

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

        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {

                currentUserName = profile.email.toString()

            }
        }
        currentUserName = currentUserName.dropLast(10)


        val ref = database.getReference("UserMeal")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(userSnapShot in snapshot.children){
                        val user = userSnapShot.getValue(UserData::class.java)
                        if(user?.user == currentUserName ){
                            userArrayList.add(user)
                        }

                    }

                    userRecylerView.adapter = UserAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "failed to load data", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.nav_search, menu)
        val search: MenuItem? = menu.findItem(R.id.nav_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search Something"

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var tempArrayList: ArrayList<UserData> = arrayListOf<UserData>()

                for(item in userArrayList){
                    if(item.name == (newText) ){
                        tempArrayList.add(item)
                    }
                }
                    if(newText.isEmpty()){
                        userRecylerView.adapter = UserAdapter(userArrayList)
                    }else {
                        userRecylerView.adapter = UserAdapter(tempArrayList)
                    }

                return true

            }

        })
        return super.onCreateOptionsMenu(menu, inflater)
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