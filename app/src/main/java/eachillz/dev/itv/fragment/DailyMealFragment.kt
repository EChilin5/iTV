package eachillz.dev.itv.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.databinding.FragmentDailyMealBinding
import eachillz.dev.itv.overlay.overlayfood
import eachillz.dev.itv.adapter.UserMealAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import eachillz.dev.itv.firestore.DailyMealPost
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet


/**
 * A simple [Fragment] subclass.
 * Use the [DailyMealFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "DailyMealFragment"
private const val PICK_PHOTO_CODE = 1234
private lateinit var photFile: File


class DailyMealFragment : Fragment() {


    private var _binding: FragmentDailyMealBinding? = null
    private val binding get() = _binding!!
    private var dateFormated = ""
    private var FILE_NAME = "photo.jpg"


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        firestoreDb = FirebaseFirestore.getInstance()

        val date = getCurrentDateTime()
        dateFormated = date.toString("MM/dd/yyyy")
        binding.tvCurrentDate.text = dateFormated

        binding.fabSave.setOnClickListener {
//            takePhoto()
            openFoodItem()
        }

        userRecylerView = binding.rvDailyFood
        userRecylerView.layoutManager = LinearLayoutManager(context)

        userMealArrayList = arrayListOf<DailyMealPost>()

//        getUserFoodData()
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
        var hashSet = HashSet<String>()
        var currentUserName = getUserEmail()
        userMealArrayList.clear()
        var calories = 10000
        var total = 0

        var mealReference = firestoreDb.collection("userDailyMeal")
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)
        mealReference = mealReference.whereEqualTo("user.email", currentUserName)

        mealReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }

            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val mealItem: DailyMealPost =
                        dc.document.toObject(DailyMealPost::class.java)

                    if (mealItem?.date!!.contains(dateFormated)) {
                        calories -= Integer.parseInt(mealItem.calories.toString())
                        total += Integer.parseInt(mealItem.calories.toString())
                        if (!hashSet.contains(mealItem.name)) {
                            userMealArrayList.add(mealItem)
                            hashSet.add(mealItem.name.toString())
                        }
                    }

                }
            }
            if (userMealArrayList.isEmpty()) {
                val temp =  DailyMealPost(
                    "", "Example User", 0,0,0,0,"",0,0, dateFormated,null  )

                userMealArrayList.add(temp)
            }
            userRecylerView.adapter = UserMealAdapter(userMealArrayList)
            binding.tvCurrentCalorieCounter.text = total.toString()
            binding.tvRemainingCaloriesCounter.text = calories.toString()

            val progress1 = ((total).toDouble() / 10000) *100
            val progress2 = 100 - progress1

            binding.progressBar.progress = progress1.toInt()
            binding.progressBar2.progress = progress2.toInt()


        }

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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyMealFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}



