package eachillz.dev.itv.firestore

import java.time.LocalDate

data class DailyMealChartData(
    var dataChecked: LocalDate,
    val calorieTrend:Int,
)