package eachillz.dev.itv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import eachillz.dev.itv.databinding.FragmentOverlayfoodBinding
import eachillz.dev.itv.user.UserItemDataEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import eachillz.dev.itv.api.FoodSearchResult
import eachillz.dev.itv.api.FoodService
import eachillz.dev.itv.api.Hint
import eachillz.dev.itv.api.Parsed
import eachillz.dev.itv.firestore.DailyMealPost
import eachillz.dev.itv.fragment.DailyMealFragment
import eachillz.dev.itv.user.User
import eachillz.dev.itv.user.UserAdapter
import eachillz.dev.itv.user.UserDailyMealPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


private const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"
private const val TAG = "DialogFragment"
class overlayfood : DialogFragment() {


    private var _binding: FragmentOverlayfoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference

//    private lateinit var database: DatabaseReference

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

        val date = getCurrentDateTime()
        val dateInString = date.toString("MM/dd/yyyy")


        binding.btnPost.setOnClickListener {
            var text = binding.etName.text.toString()
            if(text.isNotEmpty()){
                retrieveEdamanFoodInformation(text)
            }

        }
    }


    private fun retrieveEdamanFoodInformation(ingr:String){
        var nutrition_type = "cooking"
        var retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val edamanService = retrofit.create(FoodService::class.java)
        edamanService.foodInfo( getString(R.string.app_id_food_db), getString(R.string.app_key_food_db), ingr, nutrition_type)
            .enqueue(object : Callback<FoodSearchResult> {
                override fun onResponse(
                    call: Call<FoodSearchResult>,
                    response: Response<FoodSearchResult>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if(body == null){
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    Log.i(TAG, "${response.body()}")


                    var arrayParsed = mutableListOf<Hint>()
                    arrayParsed.addAll(body.hints)

                    var name = body.text
                    var protein = arrayParsed.get(0).food.nutrients.PROCNT.toLong()
                    var calories = arrayParsed.get(0).food.nutrients.ENERC_KCAL.toLong()
                    var fat = arrayParsed.get(0).food.nutrients.FAT.toLong()
                    var carbs = arrayParsed.get(0).food.nutrients.CHOCDF.toLong()
                    var image = arrayParsed.get(0).food.image
                    var time = System.currentTimeMillis()
                    val date = getCurrentDateTime()
                    val dateInString = date.toString("MM/dd/yyyy")
                    var serving = 1


                    if(image.isNullOrEmpty()){
                        image = "https://firebasestorage.googleapis.com/v0/b/textdemo-9e9b1.appspot.com/o/posts%2FFri%20Sep%2010%2015%3A52%3A09%20PDT%202021.png?alt=media&token=a774304d-da5b-4cb9-8fbc-853f8ff6a78f"
                    }

                    var emailName = getEmailName()

                    val user : User = User("", emailName )
                    var meal = DailyMealPost("",name, protein, calories, fat, carbs, image, serving, time, dateInString, user )

                    addMealDB(meal)

                }

                override fun onFailure(call: Call<FoodSearchResult>, t: Throwable) {
                    Log.e(TAG, "onFailure $t")
                }
            })
    }

    private fun addMealDB(mealPost: DailyMealPost){
        firestoreDb.collection("userDailyMeal").add(mealPost).addOnCompleteListener {
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

    private fun takePhoto(){
        val name = binding.etName.text.toString()
//        val calories = binding.etCalories.text.toString()
//            val carbs = binding.etCarbs.text.toString()
//            val protein = binding.etProtein.text.toString()
//        if(name.isNotEmpty() && calories.toIntOrNull() != null) {
//            uploadImage(name, calories, dateInString, imgPath)
//            Toast.makeText(context, "Post is Saved", Toast.LENGTH_LONG).show()
//        }else{
//            Toast.makeText(context, "Incorrect Information was provided", Toast.LENGTH_LONG).show()
//
//        }
    }

//    private fun uploadImage(name: String, calories: String?,  dateInString: String,imgPath: String?) {
//        val photoReference =
//            storageReference.child("images/"+imgPath.toString())
//        val file = Uri.fromFile(File(imgPath.toString()))
//
//        photoReference.putFile( file)
//            .continueWithTask { photoUploadTask ->
//                Log.i(TAG, "upload bytes: ${photoUploadTask.result.bytesTransferred}")
//                // retrieve image url of upload image
//                photoReference.downloadUrl
//
//            }.continueWithTask { downloadUrlTask ->
//
//                    val userName = Firebase.auth.currentUser
//                    var currentUserName = ""
//                    userName?.let {
//                        for (profile in it.providerData) {
//                            // Id of the provider (ex: google.com)
//                            currentUserName = profile.email.toString()
//                        }
//                    }
//                    val email = currentUserName
//                    currentUserName = currentUserName.dropLast(10)
//                    val dateNow = Calendar.getInstance().time
//
//                    val user : User = User("", email, currentUserName )
//                    val mealInfo: UserDailyMealPost = UserDailyMealPost("", name, downloadUrlTask.result.toString(), dateInString,System.currentTimeMillis(), calories!!.toLong(),user )
//                firestoreDb.collection("userDailyMeal").add(mealInfo)
//
//            }.addOnCompleteListener { postCreationTask ->
////                btnSubmitPost.isEnabled = true
//
//                if (!postCreationTask.isSuccessful) {
////                    Toast.makeText(context, "UPLOADED failed", Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, "failed", postCreationTask.exception)
//                }
//
//            }
//    }



    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            overlayfood().apply {
                arguments = Bundle().apply {

                }
            }
    }
}