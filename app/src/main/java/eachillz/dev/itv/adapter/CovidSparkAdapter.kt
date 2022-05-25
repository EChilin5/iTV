package eachillz.dev.itv.adapter

import android.graphics.RectF
import com.robinhood.spark.SparkAdapter
import eachillz.dev.itv.api.CovidData

class CovidSparkAdapter(private val dailydata: List<CovidData>) : SparkAdapter(){

    var metric = Metric.POSITIVE
    var daysAgo = TimeScale.MAX

    override fun getCount(): Int {
        return dailydata.size
    }

    override fun getItem(index: Int) = dailydata[index]

    override fun getY(index: Int): Float {
        val chosenDayData = dailydata[index]

        return when(metric){
            Metric.POSITIVE -> chosenDayData.positiveIncrease.toFloat()
            Metric.NEGATIVE -> chosenDayData.negativeIncrease.toFloat()
            Metric.DEATH -> chosenDayData.deathIncrease.toFloat()

        }
    }

    override fun getDataBounds(): RectF {
        val bounds =  super.getDataBounds()
        if(daysAgo !=TimeScale.MAX){
            bounds.left = count - daysAgo.numDays.toFloat()
        }

        return bounds
    }
}