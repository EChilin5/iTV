package eachillz.dev.itv.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import eachillz.dev.itv.databinding.FragmentProggressBinding
import eachillz.dev.itv.user.UserDailyMealPost
import java.util.*

import com.google.gson.GsonBuilder
import com.robinhood.ticker.TickerUtils
import eachillz.dev.itv.R
import eachillz.dev.itv.adapter.CovidSparkAdapter
import eachillz.dev.itv.adapter.Metric
import eachillz.dev.itv.adapter.TimeScale
import eachillz.dev.itv.api.CovidData
import eachillz.dev.itv.api.CovidService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.*


private const val TAG = "ProggressFragment"
private const val BASE_URL = "https://covidtracking.com/api/v1/"
class ProggressFragment : Fragment() {

    private lateinit var currentlyShownData: List<CovidData>
    private lateinit var adapter: CovidSparkAdapter
    private lateinit var perStateDailyData: Map<String, List<CovidData>>
    private lateinit var nationalDailyData: List<CovidData>
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

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val covidService = retrofit.create(CovidService::class.java)

        //fetch the national data
        covidService.getNationalData().enqueue(object: Callback<List<CovidData>>{
            override fun onResponse(
                call: Call<List<CovidData>>,
                response: Response<List<CovidData>>
            ) {
                Log.i(TAG, "onResponse $response")
                val nationalData = response.body()
                if(nationalData == null){
                    Log.w(TAG, "Did not recieve valid response ")
                    return
                }
                setUpEventListeners()
                nationalDailyData = nationalData.reversed()
                Log.i(TAG, "update graph with national data  ")
                updateDisplayWithData(nationalDailyData)
            }

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                Log.e(TAG, "failed to fetch data $t")
            }

        })

        // fetch the state data
        covidService.getStatesData().enqueue(object: Callback<List<CovidData>>{
            override fun onResponse(
                call: Call<List<CovidData>>,
                response: Response<List<CovidData>>
            ) {
                Log.i(TAG, "onResponse $response")
                val statesData = response.body()
                if(statesData == null){
                    Log.w(TAG, "Did not recieve valid response ")
                    return
                }
                perStateDailyData = statesData.reversed().groupBy { it.state }
                Log.i(TAG, "update sippnner with state data  ")
            }

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                Log.e(TAG, "failed to fetch data $t")
            }

        })
    }

    private fun setUpEventListeners() {
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList())
        // add a listener for the user scrubing on chart
        binding.sparkView.isScrubEnabled = true
        binding.sparkView.setScrubListener { itemData ->
            if(itemData is CovidData){
                updateInfoForDate(itemData)
            }

        }
        // TODO: rESPOND TO THE RADIO BUTTON SELECTED EVENTS
        binding.radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            adapter.daysAgo = when(checkedId){
                R.id.radioButtonWeek ->TimeScale.WEEK
                R.id.radioButtonMonth ->TimeScale.MONTH
                R.id.radioButtonMax ->TimeScale.MAX
                else ->TimeScale.MAX

            }
            adapter.notifyDataSetChanged()
        }
        binding.radioGroupMetricSelections.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.radioButtonPoitive ->updateDiplayMetric(Metric.POSITIVE)
                R.id.radioButtonNegative ->updateDiplayMetric(Metric.NEGATIVE)
                R.id.radioButtonDeath ->updateDiplayMetric(Metric.DEATH)

            }
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

    private fun updateDisplayWithData(dailydata: List<CovidData>) {
        currentlyShownData = dailydata
        // create a new spark adapter with data
        adapter = CovidSparkAdapter(dailydata)
        binding.sparkView.adapter = adapter
        // update the radio button to select the positive case and max time by defualt
        binding.radioButtonPoitive.isChecked = true
        binding.radioButtonMax.isChecked = true
        //display metric for the most recent date
        updateDiplayMetric(Metric.POSITIVE)
    }

    private fun updateInfoForDate(covidData: CovidData) {
        val numCases = when(adapter.metric){
            Metric.NEGATIVE->covidData.negativeIncrease
            Metric.POSITIVE->covidData.positiveIncrease
            Metric.DEATH->covidData.deathIncrease

        }
        binding.tickerView.text =  NumberFormat.getInstance().format(numCases)
        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        binding.tvDateLabel.text = outputDateFormat.format(covidData.dataChecked)
    }



}