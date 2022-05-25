package eachillz.dev.itv.adapter

import android.graphics.RectF
import com.robinhood.spark.SparkAdapter
import eachillz.dev.itv.api.CovidData
import eachillz.dev.itv.firestore.DailyMealChartData

class CovidSparkAdapter(private val dailydata: List<DailyMealChartData>) : SparkAdapter(){

    var metric = Metric.POSITIVE
    var daysAgo = TimeScale.MAX

    override fun getCount(): Int {
        return dailydata.size
    }

    override fun getItem(index: Int) = dailydata[index]

    override fun getY(index: Int): Float {
        val chosenDayData = dailydata[index]

        return  chosenDayData.calorieTrend.toFloat()
    }

    override fun getDataBounds(): RectF {
        val bounds =  super.getDataBounds()
        if(daysAgo !=TimeScale.MAX){
            bounds.left = count - daysAgo.numDays.toFloat()
        }

        return bounds
    }
}