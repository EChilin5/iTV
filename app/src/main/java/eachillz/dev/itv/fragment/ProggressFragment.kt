package eachillz.dev.itv.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import eachillz.dev.itv.databinding.FragmentProggressBinding
import eachillz.dev.itv.user.UserDailyMealPost
import java.util.*


import com.robinhood.ticker.TickerUtils
import eachillz.dev.itv.R
import eachillz.dev.itv.adapter.CalorieSparkAdapter
import eachillz.dev.itv.adapter.Metric
import eachillz.dev.itv.adapter.TimeScale
import eachillz.dev.itv.adapter.WeightAdapter
import eachillz.dev.itv.firestore.DailyMealChartData
import eachillz.dev.itv.firestore.DailyMealPost
import eachillz.dev.itv.model.WeightWatcherModal
import eachillz.dev.itv.overlay.AddWeightOverlay
import eachillz.dev.itv.overlay.overlayfood

import java.text.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap


private const val TAG = "ProggressFragment"
private const val BASE_URL = "https://covidtracking.com/api/v1/"
class ProggressFragment : Fragment() {

    private lateinit var  flBtnAddWeight: FloatingActionButton
    private lateinit var weightListener: ListenerRegistration
   // private lateinit var userCalorieListener: ListenerRegistration
    private lateinit var currentlyShownData: List<DailyMealChartData>
    private lateinit var adapter: CalorieSparkAdapter
    private  var calorieDailyData =  mutableListOf<DailyMealChartData>()
    private var weightInformation = mutableListOf<WeightWatcherModal>()
    private var _binding: FragmentProggressBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealsArray :MutableList<UserDailyMealPost>
    private lateinit var firestoreDb: FirebaseFirestore
    private var adapterWeight = WeightAdapter(weightInformation)
    private lateinit var rvWeights : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProggressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealsArray = mutableListOf()
        firestoreDb = FirebaseFirestore.getInstance()
        rvWeights = binding.rvWeight
        rvWeights.adapter = adapterWeight
        rvWeights.layoutManager = LinearLayoutManager(context)

        flBtnAddWeight = binding.flBtnAddWeight

        flBtnAddWeight.setOnClickListener {
            openAddWeightItem()
        }

        //fetchData()
        fetchWeightData()
    }


//    @SuppressLint("SimpleDateFormat", "NewApi")
//    private fun fetchData() {
//        calorieDailyData.clear()
//        var hashSet = HashMap<String, Int>()
//        var currentUserName = getUserEmail()
//
//        var calories = 10000
//        var total = 0
//
//        var mealReference = firestoreDb.collection("userDailyMeal")
//            .orderBy("date", Query.Direction.DESCENDING)
//        mealReference = mealReference.whereEqualTo("user.email", currentUserName)
//
//        userCalorieListener = mealReference.addSnapshotListener { snapshot, exception ->
//            if (exception != null || snapshot == null) {
//                Log.e(TAG, "exception occurred", exception)
//                return@addSnapshotListener
//            }
//
//            for (dc: DocumentChange in snapshot?.documentChanges!!) {
//                if (dc.type == DocumentChange.Type.ADDED) {
//
//                    val mealItem: DailyMealPost =
//                        dc.document.toObject(DailyMealPost::class.java)
//
//                        if(hashSet.containsKey(mealItem.date)){
//                            hashSet.put(mealItem.date,
//                                hashSet.get(mealItem.date)?.plus(mealItem.calories.toInt())!!
//                            )
//                        }else{
//                            hashSet.put(mealItem.date, mealItem.calories.toInt())
//                        }
//                }
//            }
//
//            for((key, value ) in hashSet){
//                var day = LocalDate.parse(key, DateTimeFormatter.ofPattern("yyyyMMdd"))
//              //  var item = DailyMealChartData(day, value)
//               // Log.e(TAG, "$item")
//               // calorieDailyData.add(item)
//            }
//            setUpEventListeners()
//            updateDisplayWithData(calorieDailyData)
//
//            val progress1 = ((total).toDouble() / 10000) *100
//
//        }
//    }

    private fun fetchWeightData(){
        val email = getUserEmail()
        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        var today = outputDateFormat.format(Date())
        Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
        var weightTracker = firestoreDb.collection("weightWatcher").whereEqualTo("user.email", email )
            .orderBy("date", Query.Direction.DESCENDING)
        weightListener = weightTracker.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }
            calorieDailyData.clear()
            weightInformation.clear()
            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val reviewItem: WeightWatcherModal = dc.document.toObject(WeightWatcherModal::class.java)
                    weightInformation.add(reviewItem)
                    var item = DailyMealChartData(reviewItem.date, reviewItem.weight)
                    calorieDailyData.add(item)
                    Log.e(TAG, " ${reviewItem.date} ++ ${Date()}")
                    var date = outputDateFormat.format(reviewItem.date)

                    if(date == today){
                        flBtnAddWeight.isVisible = false
                        flBtnAddWeight.isEnabled = false
                    }

                }
            }
            calorieDailyData.reverse()
            adapterWeight.notifyDataSetChanged()

            setUpEventListeners()
            updateDisplayWithData(calorieDailyData)

        }
    }


    private fun openAddWeightItem() {
//        val args = Bundle()
//        args.putString("Image", takenImage)
        val newFragment = AddWeightOverlay()
//        newFragment.arguments = args
        newFragment.show(childFragmentManager, "TAG")

    }


    private fun getUserEmail():String{
        val userName = Firebase.auth.currentUser
        var currentUserName = userName?.email.toString()

        return currentUserName
    }

    override fun onStop() {
        super.onStop()
        weightListener.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
        weightListener.remove()
    }



    private fun setUpEventListeners() {
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList())
        // add a listener for the user scrubing on chart
        binding.sparkView.isScrubEnabled = true
        binding.sparkView.setScrubListener { itemData ->
            if(itemData is DailyMealChartData){
                updateInfoForDate(itemData)
            }

        }
        binding.radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            adapter.daysAgo = when(checkedId){
                R.id.radioButtonWeek ->TimeScale.WEEK
                R.id.radioButtonMonth ->TimeScale.MONTH
                R.id.radioButtonMax ->TimeScale.MAX
                else ->TimeScale.MAX

            }
            adapter.notifyDataSetChanged()
        }

    }

    private fun updateDiplayMetric(metric: Metric) {
// update the color of the chart
        val colorRes = when(metric){
            Metric.DEATH ->R.color.ap_charcoal
            Metric.NEGATIVE-> R.color.fusia
            Metric.POSITIVE-> R.color.teal_200
        }
       @ColorInt val colorInt = ContextCompat.getColor(requireContext(), colorRes)
        binding.sparkView.lineColor = colorInt
        binding.tickerView.textColor = colorInt

        // update the metric on the adapter

        adapter.metric = metric
        adapter.notifyDataSetChanged()

        // reset the number and the date showin in the bottom text view
        updateInfoForDate(currentlyShownData.last())
    }

    private fun updateDisplayWithData(dailydata: MutableList<DailyMealChartData>) {
        currentlyShownData = dailydata
        // create a new spark adapter with data
        adapter = CalorieSparkAdapter(dailydata)
        binding.sparkView.adapter = adapter
        // update the radio button to select the positive case and max time by defualt

        binding.radioButtonMax.isChecked = true
        //display metric for the most recent date
        updateDiplayMetric(Metric.POSITIVE)
    }

    private fun updateInfoForDate(calorieIntake: DailyMealChartData) {
        val numCases = when(adapter.metric){
            Metric.NEGATIVE->calorieIntake.calorieTrend
            Metric.POSITIVE->calorieIntake.calorieTrend
            Metric.DEATH->calorieIntake.calorieTrend

        }
        binding.tickerView.text =  NumberFormat.getInstance().format(numCases)
        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        binding.tvDateLabel.text = outputDateFormat.format(calorieIntake.dataChecked)
    }



}