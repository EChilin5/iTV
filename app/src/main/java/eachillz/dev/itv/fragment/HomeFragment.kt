package eachillz.dev.itv.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import eachillz.dev.itv.R
import eachillz.dev.itv.databinding.ActivityFoodItemDetailBinding.inflate
import eachillz.dev.itv.databinding.FragmentHomeBinding
import eachillz.dev.itv.user.UserAdapter
import eachillz.dev.itv.user.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import eachillz.dev.itv.user.UserDailyMealPost


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */


private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    private val database = Firebase.database
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var userRecylerView: RecyclerView
    private lateinit var userMealArrayList: ArrayList<UserDailyMealPost>

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
        firestoreDb = FirebaseFirestore.getInstance()
        userRecylerView  = binding.rvHome
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userMealArrayList = arrayListOf<UserDailyMealPost>()
        getUserData()

    }



    private fun getUserData(){

        var hashSet = HashSet<String>()
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {

                currentUserName = profile.email.toString()

            }
        }
        currentUserName = currentUserName.dropLast(10)
        val mAuth = FirebaseAuth.getInstance();
        val mCurrentUserId = mAuth.getCurrentUser()?.getUid()

        var mealReference = firestoreDb.collection("userDailyMeal")
            .orderBy("creation_time_ms", Query.Direction.ASCENDING)

        mealReference = mealReference.whereEqualTo("user.username", currentUserName)

        mealReference.addSnapshotListener{snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }
            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val mealItem: UserDailyMealPost =
                        dc.document.toObject(UserDailyMealPost::class.java)
                    Toast.makeText(context, "id# ${mealItem.id}", Toast.LENGTH_SHORT).show()
                    userMealArrayList.add(mealItem)

                }
            }
            userRecylerView.adapter = UserAdapter(userMealArrayList)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.nav_search, menu)
        val search: MenuItem? = menu.findItem(R.id.nav_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "search name or calorie"

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val tempArrayList: ArrayList<UserDailyMealPost> = arrayListOf<UserDailyMealPost>()

                for(item in userMealArrayList){
                    if(item.name?.contains(newText) == true ){
                        tempArrayList.add(item)
                    }else if( newText.toIntOrNull() != null && item.calories?.toInt()!! <= newText.toInt()){
                        tempArrayList.add(item)
                    }
                }
                    if(newText.isEmpty()){
                        userRecylerView.adapter = UserAdapter(userMealArrayList)
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
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}