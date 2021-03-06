package eachillz.dev.itv.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.R
import eachillz.dev.itv.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import eachillz.dev.itv.adapter.RecipeAdapter
import eachillz.dev.itv.api.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




private const val BASE_URL = "https://api.edamam.com/api/recipes/"
private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    private val database = Firebase.database
    private lateinit var firestoreDb: FirebaseFirestore


    private val recipeResult = mutableListOf<Hit>()
    private var adapter = RecipeAdapter(recipeResult)
    private lateinit var rvRecipe: RecyclerView


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
        rvRecipe = binding.rvHome
        rvRecipe.adapter = adapter
        rvRecipe.layoutManager = LinearLayoutManager(context)
//        getUserData()

        //var text ="chicken"

        binding.ivbtnSearch.setOnClickListener {
            val text = binding.etFoodSearch.text.toString()
            rvRecipe.removeAllViews()
            rvRecipe.adapter = adapter
            threadBlockCouritinApiFetch(text)
        }

        if(recipeResult.isEmpty()){
            Log.e(TAG, "api is called first time ")
            rvRecipe.isVisible = false
            rvRecipe.isEnabled = false
            binding.ivRecipeNotFound.isVisible = true
            binding.tvRecipeNotFound.isVisible = true
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearRecyclerView() :Boolean{
        var size = recipeResult.size-1
        while(size >=0){
            recipeResult.removeAt(size)
            adapter.notifyItemRemoved(size)
            size--

        }
        recipeResult.clear()
        adapter.notifyDataSetChanged()
        Log.e(TAG, "size is ${recipeResult.size} " + System.currentTimeMillis())
        return true
    }

    fun threadBlockCouritinApiFetch(text:String) = runBlocking {
        val clear = async { clearRecyclerView() }
        if( clear.await()){
            Log.e(TAG, "$clear is called running second thread")
           retrieveEdamanFoodInformation(text)
        }

    }

    private fun retrieveEdamanFoodInformation (foodSearch :String) {
        Log.e(TAG, "api is called retrieveEdamanFoodInformation" + System.currentTimeMillis())
        val type = "public"
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val edamamRecipeService = retrofit.create(RecipeService::class.java)
        edamamRecipeService.recipeInfo(type, foodSearch, getString(R.string.app_id_recipe_db), getString(R.string.app_key_recipe_db))
            .enqueue(object : Callback<RecipeResult>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<RecipeResult>,
                    response: Response<RecipeResult>
                ) {
                    recipeResult.clear()
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if(body == null){
                        Log.i(TAG, "Did not recieve valid response from Edamam Food Service")
                        return
                    }

                    recipeResult.addAll(body.hits)
                    if(recipeResult.isNotEmpty()){
                        rvRecipe.isEnabled = true
                        rvRecipe.isVisible = true
                        binding.ivRecipeNotFound.isVisible = false
                        binding.ivRecipeNotFound.isEnabled = false
                        binding.tvRecipeNotFound.isVisible = false
                        binding.tvRecipeNotFound.isEnabled = false
                    }

                    adapter.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<RecipeResult>, t: Throwable) {
                    Log.i(TAG, "failed to get info $t")
                }

            })

    }

}