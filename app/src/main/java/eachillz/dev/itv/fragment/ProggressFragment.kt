package eachillz.dev.itv.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
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
import eachillz.dev.itv.api.CovidData
import eachillz.dev.itv.firestore.DailyMealChartData
import eachillz.dev.itv.firestore.DailyMealPost

import java.text.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap


private const val TAG = "ProggressFragment"
private const val BASE_URL = "https://covidtracking.com/api/v1/"
class ProggressFragment : Fragment() {

    private lateinit var dateFormated: String
    private lateinit var currentlyShownData: List<DailyMealChartData>
    private lateinit var adapter: CalorieSparkAdapter
    private lateinit var perStateDailyData: Map<String, List<CovidData>>
    private lateinit var nationalDailyData: List<CovidData>
    private  var calorieDailyData =  mutableListOf<DailyMealChartData>()
    private var _binding: FragmentProggressBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealsArray :MutableList<UserDailyMealPost>
    private lateinit var firestoreDb: FirebaseFirestore

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
        fetchData()

    }


    @SuppressLint("SimpleDateFormat", "NewApi")
    private fun fetchData() {
        calorieDailyData.clear()
        var hashSet = HashMap<String, Int>()
        var currentUserName = getUserEmail()

        var calories = 10000
        var total = 0

        var mealReference = firestoreDb.collection("userDailyMeal")
            .orderBy("date", Query.Direction.DESCENDING)
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

                        if(hashSet.containsKey(mealItem.date)){
                            hashSet.put(mealItem.date,
                                hashSet.get(mealItem.date)?.plus(mealItem.calories.toInt())!!
                            )
                        }else{
                            hashSet.put(mealItem.date, mealItem.calories.toInt())
                        }


                }


            }


            for((key, value ) in hashSet){

//                val formatter = SimpleDateFormat("yyyyMMdd")
//                var localDate = formatter.parse(key)
//
                var day = LocalDate.parse(key, DateTimeFormatter.ofPattern("yyyyMMdd"))
                var item = DailyMealChartData(day, value)
                Log.e(TAG, "$item")
                calorieDailyData.add(item)
            }

            setUpEventListeners()
            updateDisplayWithData(calorieDailyData)


            val progress1 = ((total).toDouble() / 10000) *100
            val progress2 = 100 - progress1

//            binding.progressBar.progress = progress1.toInt()



        }

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



//    private fun covidFetchData(){
//        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//        val covidService = retrofit.create(CovidService::class.java)
//
//        //fetch the national data
//        covidService.getNationalData().enqueue(object: Callback<List<CovidData>>{
//            override fun onResponse(
//                call: Call<List<CovidData>>,
//                response: Response<List<CovidData>>
//            ) {
//                Log.i(TAG, "onResponse $response")
//                val nationalData = response.body()
//                if(nationalData == null){
//                    Log.w(TAG, "Did not recieve valid response ")
//                    return
//                }
//                setUpEventListeners()
//                nationalDailyData = nationalData.reversed()
//                Log.i(TAG, "update graph with national data  ")
//                //updateDisplayWithData(nationalDailyData)
//            }
//
//            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
//                Log.e(TAG, "failed to fetch data $t")
//            }
//
//        })
//
//        // fetch the state data
//        covidService.getStatesData().enqueue(object: Callback<List<CovidData>>{
//            override fun onResponse(
//                call: Call<List<CovidData>>,
//                response: Response<List<CovidData>>
//            ) {
//                Log.i(TAG, "onResponse $response")
//                val statesData = response.body()
//                if(statesData == null){
//                    Log.w(TAG, "Did not recieve valid response ")
//                    return
//                }
//                perStateDailyData = statesData.reversed().groupBy { it.state }
//                Log.i(TAG, "update sippnner with state data  ")
//            }
//
//            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
//                Log.e(TAG, "failed to fetch data $t")
//            }
//
//        })
//    }

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
        binding.tvDateLabel.text = calorieIntake.dataChecked.toString()
    }



}