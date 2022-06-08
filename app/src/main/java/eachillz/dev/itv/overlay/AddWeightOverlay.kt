package eachillz.dev.itv.overlay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import eachillz.dev.itv.databinding.FragmentAddWeightOverlayBinding
import eachillz.dev.itv.firestore.DailyMealPost
import eachillz.dev.itv.model.WeightWatcherModal
import eachillz.dev.itv.user.User
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "AddWeightOverlay"
class AddWeightOverlay(var onUpdate: () -> Unit) : DialogFragment() {

    private lateinit var userCalorieListener: ListenerRegistration
    private  var _binding: FragmentAddWeightOverlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestoreDB :FirebaseFirestore

    private lateinit var btnSubmit:Button
    private lateinit var etWeight : TextView

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddWeightOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreDB = FirebaseFirestore.getInstance()
        btnSubmit = binding.btnSubmitWeight
        etWeight = binding.etNewWeight
        btnSubmit.setOnClickListener {
            if(etWeight.text.isNotEmpty()){
                fetchCalorieData()
            }
        }
    }

    private fun fetchCalorieData() {
        val currentUserName = getEmail()
        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val currentDay = outputDateFormat.format(Date())


        val mealReference =
            firestoreDB.collection("userDailyMeal").whereEqualTo("user.email", currentUserName)


        userCalorieListener = mealReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }

            Log.e(TAG, "${snapshot.size()}")
            var total = 0
            for (dc: DocumentChange in snapshot.documentChanges) {
                val mealItem: DailyMealPost = dc.document.toObject(DailyMealPost::class.java)
                if(currentDay == outputDateFormat.format(mealItem.date)){
                    total+= mealItem.calories.toInt()

                }
//                if (dc.type == DocumentChange.Type.ADDED) {

//
//
//                    if (CalorieHashMap.containsKey(currentDay)) {
//                        CalorieHashMap.put(
//                            currentDay,
//                            CalorieHashMap.get(currentDay)?.plus(mealItem.calories.toInt())!!
//                        )
//                    } else {
//                        CalorieHashMap.put(currentDay, mealItem.calories.toInt())
//                    }
//                }
            }
            Log.e(TAG, total.toString())
            addNewWeight(etWeight.text.toString(), total)
        }
    }

    private fun addNewWeight(weight:String, calories:Int){
        val email = getEmail()
        val user = User("", email )
        val newWeight = WeightWatcherModal("", weight.toInt(), calories, Date(), user )
        firestoreDB.collection("weightWatcher").add(newWeight).addOnCompleteListener {
            if(it.isSuccessful){
                Log.e(TAG, "added new weight")
                this.dismiss()
                onUpdate()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        userCalorieListener.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
        userCalorieListener.remove()
    }

    private fun getEmail():String{
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser?.email.toString()
    }
}