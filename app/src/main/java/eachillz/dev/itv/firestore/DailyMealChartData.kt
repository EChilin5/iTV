package eachillz.dev.itv.firestore

import android.support.annotation.Keep

@Keep
data class DailyMealChartData(
    var dataChecked: String,
    val calorieTrend:Int,
)