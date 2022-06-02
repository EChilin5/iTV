package eachillz.dev.itv.firestore

import java.time.LocalDate
import java.util.*

data class DailyMealChartData(
    var dataChecked: Date,
    val calorieTrend:Int,
)