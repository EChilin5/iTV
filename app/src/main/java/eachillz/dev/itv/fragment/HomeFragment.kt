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
import eachillz.dev.itv.api.*
import eachillz.dev.itv.firestore.DailyMealPost
import eachillz.dev.itv.user.UserDailyMealPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import eachillz.dev.itv.model.Post
import eachillz.dev.itv.user.User


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

private const val BASE_URL = "https://api.edamam.com/api/recipes/"
private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    private val database = Firebase.database
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var userRecylerView: RecyclerView

    private val recipeResult = mutableListOf<Hit>()


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
//        getUserData()

        retrieveEdamanFoodInformation("chicken")
    }

    private fun retrieveEdamanFoodInformation(foodSearch :String){
        val type = "public"
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val edamamRecipeService = retrofit.create(RecipeService::class.java)
        edamamRecipeService.recipeInfo(type, foodSearch, getString(R.string.app_id_recipe_db), getString(R.string.app_key_recipe_db))
            .enqueue(object : Callback<RecipeResult>{
                override fun onResponse(
                    call: Call<RecipeResult>,
                    response: Response<RecipeResult>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if(body == null){
                        Log.i(TAG, "Did not recieve valid response from Edamam Food Service")
                        return
                    }

                    recipeResult.addAll(body.hits)

                    Log.i(TAG, "${recipeResult.size}")

                }

                override fun onFailure(call: Call<RecipeResult>, t: Throwable) {
                    Log.i(TAG, "failed to get info $t")
                }

            })
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