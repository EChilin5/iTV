package eachillz.dev.itv.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.databinding.FragmentDailyMealBinding
import eachillz.dev.itv.overlay.overlayfood
import eachillz.dev.itv.adapter.UserMealAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import eachillz.dev.itv.R
import eachillz.dev.itv.firestore.DailyMealPost
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet


private const val TAG = "DailyMealFragment"
class DailyMealFragment : Fragment() {


    private lateinit var mealListener: ListenerRegistration
    private var _binding: FragmentDailyMealBinding? = null
    private val binding get() = _binding!!
    private var dateFormated = ""




    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var userRecylerView: RecyclerView
    private lateinit var userMealArrayList: ArrayList<DailyMealPost>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        firestoreDb = FirebaseFirestore.getInstance()

        val date = getCurrentDateTime()
        dateFormated = date.toString("yyyyMMdd")

        binding.fabSave.setOnClickListener {
            openFoodItem()
        }

        userRecylerView = binding.rvDailyFood
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userMealArrayList = arrayListOf<DailyMealPost>()

        val outputDateFormat = SimpleDateFormat("MM/dd", Locale.US)
        val currentDay = outputDateFormat.format(Date())


        binding.tvFoodLogTitle.text = currentDay + " "+getString(R.string.food_log)
        if(userMealArrayList.isEmpty()){
            binding.rvDailyFood.isEnabled = false
            binding.rvDailyFood.isVisible = false
        }

        fetchData()


    }





    private fun openFoodItem() {
//        val args = Bundle()
//        args.putString("Image", takenImage)
        val newFragment = overlayfood()
//        newFragment.arguments = args
        newFragment.show(childFragmentManager, "TAG")

    }

    private fun getUserEmail():String{
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {

                currentUserName = profile.email.toString()

            }
        }
        return currentUserName
    }

    private fun fetchData() {
        val hashSet = HashSet<String>()
        val currentUserName = getUserEmail()
        userMealArrayList.clear()
        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val currentDay = outputDateFormat.format(Date())

        var calories = 10000
        var proteinCount = 0
        var fatCount = 0
        var carbCount = 0
        var total = 0

        var mealReference = firestoreDb.collection("userDailyMeal")
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)
        mealReference = mealReference.whereEqualTo("user.email", currentUserName)

         mealListener = mealReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }



            for (dc: DocumentChange in snapshot.documentChanges) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val mealItem: DailyMealPost =
                        dc.document.toObject(DailyMealPost::class.java)
                        val date = outputDateFormat.format(mealItem.date)
                    if (date == currentDay) {
                        calories -= Integer.parseInt(mealItem.calories.toString())
                        total += Integer.parseInt(mealItem.calories.toString())
                        proteinCount += mealItem.protein.toInt()
                        carbCount += mealItem.carbs.toInt()
                        fatCount += mealItem.fat.toInt()
                        if (!hashSet.contains(mealItem.name)) {
                            userMealArrayList.add(mealItem)
                            hashSet.add(mealItem.name)
                        }
                    }

                }
            }
//            if (userMealArrayList.isEmpty()) {
//                val temp = DailyMealPost(
//                    "", "Example User", 0, 0, 0, 0, "", 0, 0, dateFormated, null
//                )
//
//                userMealArrayList.add(temp)
//            }

             if(userMealArrayList.isNotEmpty()){
                 binding.ivSalySearch.isVisible = false
                 binding.ivNoMeals.isVisible = false
                 binding.ivPleaseAdd.isVisible = false
                 binding.ivSalySearch.isEnabled = false
                 binding.ivPleaseAdd.isEnabled = false
                 binding.ivNoMeals.isEnabled = false
                 binding.rvDailyFood.isEnabled = true
                 binding.rvDailyFood.isVisible = true
             }
            userRecylerView.adapter = UserMealAdapter(userMealArrayList)
//            binding.tvCurrentCalorieCounter.text = total.toString()
            binding.tvRemainingCaloriesCounter.text = calories.toString()

            val progress1 = ((total).toDouble() / 10000) * 100
            val progress2 = 100 - progress1

//            binding.progressBar.progress = progress1.toInt()
            binding.progressBar2.progress = progress2.toInt()
            binding.tvHProteinAmt.text = proteinCount.toString()
            binding.tvHcarbsAmt.text = carbCount.toString()
            binding.tvHFatAmnt.text = fatCount.toString()


        }


    }


    override fun onStop() {
        super.onStop()
        mealListener.remove()

        Log.e(TAG, "item removed ")
    }





//    private fun getUserFoodData(){
//        var hashSet = HashSet<String>()
//        val userName = Firebase.auth.currentUser
//        var currentUserName = ""
//        userName?.let {
//            for (profile in it.providerData) {
//
//                currentUserName = profile.email.toString()
//
//            }
//        }
//        currentUserName = currentUserName.dropLast(10)
//
//
//        val ref = database.getReference("UserMeal")
//        ref.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if(snapshot.exists()){
//                    userArrayList.clear()
//                    calories = 10000
//                    total = 0
//
//                    for(userSnapShot in snapshot.children){
//                            val user = userSnapShot.getValue(UserItemDataEntry::class.java)
//                        if(user?.date!!.contains(dateFormated) && user.user == currentUserName) {
//                            calories -= Integer.parseInt( user.calories.toString())
//                            total +=  Integer.parseInt( user.calories.toString())
//                            if(!hashSet.contains(user.name)) {
//                                userArrayList.add(user)
//                                hashSet.add(user.name.toString())
//                            }
//                            }
//
//                    }
//                    if(userArrayList.isEmpty()){
//                        val temp = UserItemDataEntry( "Example User" ,  "Select + to add your meal","0",
//                            dateFormated)
//
//                     userArrayList.add(temp)
//                    }
//
//                    userRecylerView.adapter = UserMealAdapter(userArrayList)
//                }
//
//                binding.tvCurrentCalorieCounter.text = total.toString()
//                binding.tvRemainingCaloriesCounter.text = calories.toString()
//
//                val progress1 = ((total).toDouble() / 10000) *100
//                val progress2 = 100 - progress1
//
//                binding.progressBar.progress = progress1.toInt()
//                binding.progressBar2.progress = progress2.toInt()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })
//    }



    private fun getCurrentDateTime():Date {
        return Calendar.getInstance().time
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}



