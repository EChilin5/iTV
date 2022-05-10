package eachillz.dev.itv.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidplot.xy.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import eachillz.dev.itv.databinding.FragmentProggressBinding
import eachillz.dev.itv.user.UserDailyMealPost
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import com.androidplot.xy.XYGraphWidget

import com.androidplot.xy.CatmullRomInterpolator

import com.androidplot.util.PixelUtils

import android.graphics.DashPathEffect

import android.R

import com.androidplot.xy.LineAndPointFormatter

import com.androidplot.xy.SimpleXYSeries

import com.androidplot.xy.XYSeries





private const val TAG = "ProggressFragment"
class ProggressFragment : Fragment() {

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
        getUserData()

        binding.btnWeekly.setOnClickListener {
            Toast.makeText(context,"open", Toast.LENGTH_SHORT).show()
            //setChart(weeklyDomainLabels, weeklyCalorieCount)
            getWeeklyInfo()
        }

        setChart()

    }

    private fun getUserData(){

        var hashSet = HashSet<String>()
        val userName = Firebase.auth.currentUser
        var currentUserName = ""
        userName?.let {
            for (profile in it.providerData) {

                currentUserName = profile.email.toString()

            }
        }
        currentUserName = currentUserName.dropLast(10)
        val mAuth = FirebaseAuth.getInstance();
        val mCurrentUserId = mAuth.getCurrentUser()?.getUid()

        var mealReference = firestoreDb.collection("userDailyMeal")
            .whereEqualTo("user.username", currentUserName)

        mealReference.addSnapshotListener{snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "exception occurred", exception)
                return@addSnapshotListener
            }
            for (dc: DocumentChange in snapshot?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val mealItem: UserDailyMealPost =
                        dc.document.toObject(UserDailyMealPost::class.java)
                    Toast.makeText(context, "id# ${mealItem.id}", Toast.LENGTH_SHORT).show()
                    mealsArray.add(mealItem)

                }
            }
            getWeeklyInfo()
        }
    }


    private fun getWeeklyInfo(){

        val weeklyDomainLabels = arrayOf("Sun", "Mon", "Tue", "Wed", "Th", "Fri", "Sat")
        var weeklyCalorieCount = arrayOf<Number>(0, 1, 4, 3, 7, 2,6)

//        for(item in mealsArray){
//            var cal : Calendar = Calendar.getInstance()
//            var formatDate = SimpleDateFormat("MM/dd/yyyy")
//            var day = formatDate.parse(item.date)
//            cal.time = day
//            Log.i(TAG, "${cal.get(Calendar.DAY_OF_WEEK)}  actual date ${item.date} " )
//            var index = cal.get(Calendar.DAY_OF_WEEK) -1
//            weeklyCalorieCount[index] = weeklyCalorieCount[index].toLong() + item.calories
//
//        }

      //  setChart(weeklyDomainLabels, weeklyCalorieCount)

    }

    private fun setChart(){
                //(weeklyDomainLabels: Array<String>, weeklyCalorieCount: Array<Number>) {
        val weeklyDomainLabels = arrayOf("Sun", "Mon", "Tue", "Wed", "Th", "Fri", "Sat")
        var weeklyCalorieCount = arrayOf<Number>(0, 1, 4, 3, 7, 2,6)

        var plot : XYPlot = binding.plot


        val xLabels = arrayOf<Number>(0,1,2,3,4,5,6)
        val domainLabels =  arrayOf<String>("Sun", "Mon", "Tue", "Wed", "Th", "Fri", "Sat")
        val series1Numbers = arrayOf<Number>(1, 4, 2, 8, 4, 16, 8, 32, 16, 64)
        val series2Numbers = arrayOf<Number>(5, 2, 10, 5, 20, 10, 40, 20, 80, 40)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        val series : XYSeries = SimpleXYSeries( Arrays.asList(*series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")

        val series2: XYSeries = SimpleXYSeries(
            Arrays.asList(*series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2"
        )

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        val series1Format = LineAndPointFormatter(Color.BLUE, Color.RED, null ,null)

        val series2Format = LineAndPointFormatter(Color.GREEN, Color.CYAN, null, null)




        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.interpolationParams =
            CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        series2Format.interpolationParams =
            CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        // add a new series' to the xyplot:

        // add a new series' to the xyplot:
        plot.addSeries(series, series1Format)
        plot.addSeries(series2, series2Format)

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(object : Format() {
            override fun format(
                obj: Any,
                toAppendTo: StringBuffer,
                pos: FieldPosition
            ): StringBuffer {
                val parsedInt = Math.round(obj.toString().toFloat()).toInt()
                Log.d("test", "$parsedInt $toAppendTo $pos")
                val labelString: String = domainLabels[parsedInt]
                toAppendTo.append(labelString)
                return toAppendTo
            }

            override fun parseObject(arg0: String, arg1: ParsePosition?): Any? {
                return Arrays.asList<Any>(xLabels).indexOf(arg0)
            }
        })
     }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProggressFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}