package eachillz.dev.itv.overlay

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import eachillz.dev.itv.databinding.FragmentOverlayfoodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

import android.util.Log
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import eachillz.dev.itv.R
import eachillz.dev.itv.adapter.FoodResultAdapter
import eachillz.dev.itv.api.FoodSearchResult
import eachillz.dev.itv.api.FoodService
import eachillz.dev.itv.firestore.DailyMealPost
import eachillz.dev.itv.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"
private const val TAG = "DialogFragment"
class overlayfood : DialogFragment() {


    private var _binding: FragmentOverlayfoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference

//    private lateinit var database: DatabaseReference

    private  var foodResult = mutableListOf<DailyMealPost>()
    private var adapter = FoodResultAdapter(foodResult, ::insertItem)
    private lateinit var rvFoodResult :RecyclerView



    override fun onStart() {
        super.onStart()
        dialog?.getWindow()?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOverlayfoodBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreDb = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        rvFoodResult = binding.rvResults
        rvFoodResult.adapter = adapter
        rvFoodResult.layoutManager = LinearLayoutManager(context)

        binding.clFoodResult.isVisible = false




        binding.btnPost.setOnClickListener {
            val search = binding.etName.text.toString()
            val size = binding.etServingSize.text.toString()

            if(size.isEmpty() && search.isEmpty()){
                return@setOnClickListener
            }else{
                rvFoodResult.removeAllViews()
                rvFoodResult.adapter = adapter
                retrieveEdamanFoodInformation(search, size.toInt())
            }


        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveEdamanFoodInformation(ingr: String, servingSize: Int){
        foodResult.clear()
        adapter.notifyDataSetChanged()
        val nutrition_type = "cooking"
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val edamanService = retrofit.create(FoodService::class.java)
        edamanService.foodInfo( getString(R.string.app_id_food_db), getString(R.string.app_key_food_db), ingr, nutrition_type)
            .enqueue(object : Callback<FoodSearchResult> {
                override fun onResponse(
                    call: Call<FoodSearchResult>,
                    response: Response<FoodSearchResult>
                ) {
                    foodResult.clear()
                    binding.clFoodResult.isVisible = true
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if(body == null){
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    Log.i(TAG, "${response.body()}")

                    val user = User("","")
                    for(item in body.hints){
                        val name = item.food.label
                        val protein = item.food.nutrients.PROCNT.toLong().times(servingSize)
                        val calories = item.food.nutrients.ENERC_KCAL.toLong().times(servingSize)
                        val fat = item.food.nutrients.FAT.toLong().times(servingSize)
                        val carbs = item.food.nutrients.CHOCDF.toLong().times(servingSize)
                        var image = item.food.image
                        val time = System.currentTimeMillis()
                        val serving = servingSize


                        if(image.isEmpty()){
                            image = "https://firebasestorage.googleapis.com/v0/b/textdemo-9e9b1.appspot.com/o/posts%2FFri%20Sep%2010%2015%3A52%3A09%20PDT%202021.png?alt=media&token=a774304d-da5b-4cb9-8fbc-853f8ff6a78f"
                        }

                        val meal = DailyMealPost("",name, protein, calories, fat, carbs, image, serving, time, Date(), user )

                        foodResult.add(meal)

                    }

                    adapter.notifyDataSetChanged()
                    return

                }

                override fun onFailure(call: Call<FoodSearchResult>, t: Throwable) {
                    Log.e(TAG, "onFailure $t")
                }
            })


    }

    private fun addMealDB(mealPost: DailyMealPost){
        val addMeal = firestoreDb.collection("userDailyMeal")
            addMeal.add(mealPost).addOnCompleteListener {
            if(it.isSuccessful){
                this.dismiss()
            }
        }
    }

    private fun getEmailName():String{
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                    currentUserName = profile.email.toString()
            }
        }

        return currentUserName
    }

    private fun insertItem(dailyMeal : DailyMealPost){
        val emailName = getEmailName()

        val user = User("", emailName )
        dailyMeal.user = user
        addMealDB(dailyMeal)
    }

}